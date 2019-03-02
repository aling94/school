module Main where

import Text.ParserCombinators.Parsec hiding (Parser)
import Text.Parsec.Prim
import Data.Functor.Identity
import qualified Data.HashMap.Strict as H

fixMe = undefined

-- Datatypes
-- ---------

-- Exp data type (hold parse results before evaluation)
data Exp = IntExp Integer
         | SymExp String
         | SExp [Exp]
         deriving (Show)

-- Val data type (for results of evaluation)
data Val = IntVal Integer
         | SymVal String
         | PrimVal ([Val]->Val)
         | Closure [String] Exp Env
         | DefVal String Val
         | ConsVal Val Val
         | Macro [String] Exp Env
         | ExnVal String

-- ExnVals
badVars = ExnVal "*** Scheme-Exception: Must use only `SymExp` for parameter names." 
         
-- Parsers
-- -------

-- Pretty name for Parser types
type Parser = ParsecT String () Identity

-- Lexicals

idenChars = ['a'..'z'] ++ ['A'..'Z'] ++ ['-', '*', '+', '/', ':', '\'', '?', '>', '<', '=', '!']

adigit :: Parser Char
adigit = oneOf ['0'..'9']

digits :: Parser String
digits = many1 adigit

whitespace :: Parser String
whitespace = many $ oneOf [' ', '\t', '\n']

idenFirst :: Parser Char
idenFirst = oneOf $ idenChars

idenRest :: Parser String
idenRest = many $ oneOf (idenChars ++ ['0'..'9'])

identifier :: Parser String
identifier = do whitespace
                f <- idenFirst
                r <- idenRest
                whitespace
                return (f:r)

-- Grammaticals

anInt :: Parser Exp
anInt = do whitespace
           d <- digits
           whitespace
           return $ IntExp (read d)

aSym :: Parser Exp
aSym = do whitespace
          s <- identifier
          whitespace
          return $ SymExp s

aForm :: Parser Exp
aForm = do whitespace
           char '('
           whitespace
           xs <- many anExp
           whitespace
           char ')'
           whitespace
           return $ SExp xs

aQuote :: Parser Exp
aQuote = do whitespace
            char '\''
            e <- anExp
            whitespace
            return $ SExp [SymExp "quote", e]
            
aComma :: Parser Exp
aComma = do whitespace
            char ','
            whitespace
            e <- anExp
            whitespace
            return $ SExp [SymExp "unquote", e]
            
aQuasi :: Parser Exp
aQuasi = do whitespace
            char '`'
            e <- anExp
            whitespace
            return $ SExp [SymExp "quasiquote", e]

-- Remember to modify `anExp` as to make it aware of Exp parsers you add
anExp :: Parser Exp
anExp = do whitespace
           e <- aQuasi <|> aComma <|> aQuote <|> anInt <|> aSym <|> aForm
           return e
-- Environment
-- -----------

-- Lift/Lower Haskell Bool to/from a boolean value in our Scheme representation
liftbool :: Bool -> Val
liftbool False = SymVal "nil"
liftbool True  = SymVal "t"

lowerbool :: Val -> Bool
lowerbool (SymVal "nil") = False
lowerbool _              = True

-- Lift/Lower a Haskell Int to/from an int value in our Scheme representation
liftint :: Integer -> Val
liftint = IntVal

lowerint :: Val -> Integer
lowerint (IntVal i) = i
lowerint _          = error "Cannot lower, not an IntVal!"

-- Lift/Lower a Haskell List to/from a cons value in our Scheme representation
liftlist :: [Val] -> Val
liftlist = foldr (ConsVal) (SymVal "nil")

lowerlist :: Val -> [Val]
lowerlist (ConsVal x y)  = x : lowerlist y
lowerlist (SymVal "nil") = []
lowerlist x              = [x]

-- These are helpers which take a Haskell operator (like `(+)`), a base-case
-- (what's the base-case for `(+)`?), and turn them into a suitable `PrimVal` to
-- be used by our Scheme.
liftIntOp :: (Integer -> Integer -> Integer) -> Integer -> Val
liftIntOp f z = PrimVal (\xs -> case xs of
                                   [] -> liftint z
                                   _  -> liftint (foldl1 f (map lowerint xs)) )

liftIntBoolOp :: (Integer -> Integer -> Bool) -> Val
liftIntBoolOp f = PrimVal (\xs -> let ints  = map lowerint xs
                                      bools = zipWith f ints (tail ints)
                                  in liftbool $ and bools )

liftBoolOp :: ([Bool] -> Bool) -> Val
liftBoolOp f = PrimVal (\xs -> liftbool $ f (map lowerbool xs) )

liftNot :: Val
liftNot = PrimVal (\xs -> case xs of
   [x] -> liftbool $ not (lowerbool x)
   _   -> ExnVal "*** Scheme-Exception: Incorrect number of arguments. not form is unary.")

eqAll :: Val
eqAll = PrimVal (\xs -> liftbool $ and $ zipWith isEq xs (tail xs) )
   where isEq (IntVal x) (IntVal y) = x == y
         isEq (SymVal a) (SymVal b) = a == b
         isEq _          _          = False

car :: Val
car = PrimVal (\xs -> case xs of
   [ConsVal x y] -> x
   [z] -> ExnVal ("*** Scheme-Exception: Not a cons cell: " ++ show z)
   _   -> ExnVal "*** Scheme-Exception: `car` is a unary operator." )

cdr :: Val
cdr = PrimVal (\xs -> case xs of
   [ConsVal x y] -> y
   [z] -> ExnVal ("*** Scheme-Exception: Not a cons cell: " ++ show z)
   _   -> ExnVal "*** Scheme-Exception: `cdr` is a unary operator." )
                      
-- Pretty name for the Env type
type Env = H.HashMap String Val

-- The `runtime` is the initial environment for evaluation. Primitive operators
-- such as "+", "eq?", and "cdr" must be inserted into this runtime.
runtime :: Env
runtime = H.fromList    [ ("+", liftIntOp (+) 0)
                        , ("-", liftIntOp (-) 0)
                        , ("*", liftIntOp (*) 1)
                        , ("<", liftIntBoolOp (<))
                        , (">", liftIntBoolOp (>))
                        , ("=", liftIntBoolOp (==))
                        , ("<=", liftIntBoolOp (<=))
                        , (">=", liftIntBoolOp (>=))
                        , ("!=", liftIntBoolOp (/=))
                        , ("and", liftBoolOp (and))
                        , ("or", liftBoolOp (or))
                        , ("not", liftNot)
                        , ("eq?", eqAll)
                        , ("list", PrimVal liftlist)
                        , ("car", car)
                        , ("cdr", cdr)
                        ]

-- Evaluation
-- ----------

-- Helpers

isLetList :: [Exp] -> Bool
isLetList es = and $ map isLet es
   where isLet (SExp [(SymExp v), ex]) = True
         isLet _                       = False

isVarList :: [Exp] -> Bool
isVarList es = and $ map isSym es
   where isSym (SymExp s) = True
         isSym _          = False

extVars :: [Exp] -> [String]
extVars es = map (\(SymExp s) -> s) es

quote :: Exp -> Val
quote (IntExp i) = IntVal i
quote (SymExp x) = SymVal x
quote (SExp ss)  = liftlist $ map (\e -> quote e) ss
                     
unQuote :: Val -> Exp
unQuote(IntVal i)        = IntExp i
unQuote(SymVal s)        = SymExp s
unQuote(c@(ConsVal _ _)) = SExp $ map unQuote $ lowerlist c

quasi :: Int -> Env -> Exp -> Val
quasi _ env (IntExp i) = IntVal i
quasi _ env (SymExp x) = SymVal x
quasi 1 env (SExp [SymExp "unquote"   , e]) = eval e env
quasi c env (SExp [SymExp "unquote"   , e]) = liftlist [SymVal "unquote", quasi (c-1) env e]
quasi c env (SExp [SymExp "quasiquote", e]) = liftlist [SymVal "quasiquote", (quasi (c+1) env e)]
quasi c env (SExp ss)                       = liftlist $ map (\e -> quasi c env e) ss

               
-- This `eval` must handle every way an `Exp` could be constructed.
eval :: Exp -> Env -> Val
eval (IntExp i) env = IntVal i

eval (SymExp s) env = case H.lookup s env of
                        Just f  -> f
                        Nothing -> ExnVal ("*** Scheme-Exception: Symbol " ++ s ++ " has no value.")

eval (SExp []) env  = SymVal "nil"

eval (SExp [SymExp "cons", x, y]) env = ConsVal (eval x env) (eval y env)

eval (SExp [SymExp "quote", e]) env = quote e      

eval (SExp [SymExp "quasiquote", e]) env = quasi 1 env e

eval (SExp [SymExp "eval", e]) env = case eval e env of
   c@(ConsVal _ _) -> eval (unQuote c) env
   res             -> res
   
eval (SExp [SymExp "def", SymExp name , e]) env = DefVal name (eval e env)

eval (SExp [SymExp "define", SymExp name, SExp ss,  e]) env
   | isVarList ss =
        let clo = Closure (extVars ss) e (H.insert name clo env)
        in  DefVal name clo
   | otherwise    = badVars
      
eval (SExp [SymExp "defmacro", SymExp name, SExp ss,  e]) env
   | isVarList ss =
        let mac = Macro (extVars ss) e (H.insert name mac env)
        in  DefVal name mac
   | otherwise    = badVars

eval (SExp [SymExp "let", SExp ss, e]) env
   | isLetList ss =
        let lets = map (\(SExp [(SymExp v), ex]) -> (v, eval ex env)) ss
        in  eval e (H.union (H.fromList lets) env)
   | otherwise   = ExnVal "*** Scheme-Exception: Invalid assignment in let."
   
eval (SExp [SymExp "lambda", SExp ss, e]) env
   | isVarList ss = Closure (extVars ss) e env
   | otherwise    = badVars
                     
eval (SExp [SymExp "cond", SExp args]) env = aux args
   where aux (c:e:xs)
           | lowerbool $ eval c env = eval e env
           | otherwise              = aux xs
         aux _                      = SymVal "nil"
      
eval (SExp (exp:params)) env = case eval exp env of
   PrimVal f  -> f $ map (\x -> eval x env) params
   
   Closure xs f cenv -> eval f (H.union cenv env')
      where env' = H.fromList $ zip xs (map (\e -> eval e env) params)
      
   Macro xs f menv -> eval ( unQuote $ eval f (H.union menv env') ) env
      where env' = H.fromList $ zip xs (map (\e -> quote e) params)

   res -> res
   
-- Printing
-- --------

-- This `show` must handle every way a `Val` could be constructed.
instance Show Val where
    show (IntVal i)         = show i
    show (SymVal s)         = s
    show (PrimVal _)        = "*primitive*"
    show (DefVal name _)    = name
    show (Closure _ _ _)    = "*closure*"
    show (ConsVal x y)      = "(" ++ show x ++ aux y ++ ")"
       where aux z = case z of
                     SymVal "nil" -> " "
                     ConsVal a b  -> " " ++ show a ++ aux b
                     _            -> " . " ++ show z

    show (Macro _ _ _)      = "*macro*"
                       
    show (ExnVal e)         = e


-- REPL
-- ----

-- The line with `EVAL, PRINT` is where valid parses make it through to.
-- Remember to modify this so that the result of `eval` is checked. There is
-- special behavior in the REPL if the evaluation returns a `DefVal`
repl :: Env -> IO ()
repl env =
    do  putStr "scheme> "
        l <- getLine                                                        -- READ
        case parse anExp "Expression" l of                                  -- READ
            Right exp -> case eval exp env of                               -- EVAL, PRINT
               DefVal name f ->  do putStrLn name
                                    repl (H.insert name f env)
               e             ->  do putStrLn $ show e
                                    repl env  
            Left pe   -> putStrLn (show pe)                                 -- PRINT
        repl env                                                            -- LOOP
