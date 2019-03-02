{-----------------------------------
 - Interpreter.hs
 - v1.0
 -----------------------------------}

module Interpreter where

-- Language Representation
import Data.HashMap.Strict as H
import Text.ParserCombinators.Parsec
import Parser

fixMe = error "fix me!"

liftIntOp op (IntVal x) (IntVal y) = IntVal $ op x y
liftIntOp _ _ _ = ExnVal "Cannot lift"

liftBoolOp op (BoolVal x) (BoolVal y) = BoolVal $ op x y
liftBool _ _ _ = ExnVal "Cannot lift"

liftCompOp :: (Int -> Int -> Bool) -> Val -> Val -> Val
liftCompOp op (IntVal x) (IntVal y) = BoolVal $ op x y
liftCompOp _ _ _ = ExnVal "Cannot lift"

{-----------------------------------
 - eval: The Evaluator
 -----------------------------------}
eval :: Exp -> Env -> Val
eval (IntExp i) _ = (IntVal i)
eval (BoolExp b) _ = (BoolVal b)

eval (VarExp v) env =
    case H.lookup v env of
        Just a  -> a
        Nothing -> ExnVal "No match in env"

eval (IntOpExp op e1 e2) env =
    case H.lookup op intOps of
        Just f  -> let v1 = eval e1 env
                       v2 = eval e2 env
                   in liftIntOp f v1 v2
        Nothing -> ExnVal "No matching operator"

eval (BoolOpExp op e1 e2) env =
    case H.lookup op boolOps of
        Just f  -> let v1 = eval e1 env
                       v2 = eval e2 env
                   in liftBoolOp f v1 v2
        Nothing -> ExnVal "No matching operator"

eval (CompOpExp op e1 e2) env =
    case H.lookup op compOps of
        Just f  -> let v1 = eval e1 env
                       v2 = eval e2 env
                   in liftCompOp f v1 v2
        Nothing -> ExnVal "No matching operator"

eval (LetExp ls exp) env =
    let env' = Prelude.map (\(var, e) -> (var, eval e env)) ls
    in eval exp (H.union (fromList env') env)

eval (IfExp e1 e2 e3) env =
    case eval e1 env of
        BoolVal True  -> eval e2 env
        BoolVal False -> eval e3 env
        _             -> ExnVal "Guard is not a boolean."

eval (FunExp vars body) env = CloVal vars body env

eval (AppExp exp es) env =
    case eval exp env of
        CloVal xs f env' -> let vs   = Prelude.map (\e -> eval e env) es
                                cenv = fromList (Prelude.zip xs vs)
                            in eval f (H.union env' cenv)
        _                -> ExnVal "Expression not a closure."
   
--eval _ _ = fixMe


{-----------------------------------
 - exec
 -----------------------------------}
exec :: Stmt -> PEnv -> Env -> Result
exec (PrintStmt e) penv env = (val, penv, env)
    where val = show $ eval e env

exec (SeqStmt ss) penv env = aux ("", penv, env) ss
    where aux res [] = res
          aux (p, penv', env') (x:xs) = aux ((p ++ p'), penv'', env'') xs
            where (p', penv'', env'') = exec x penv' env'

exec (IfStmt exp s1 s2) penv env =
    case (eval exp env) of
        BoolVal True  -> exec s1 penv env
        BoolVal False -> exec s2 penv env
        _             -> ("Guard is not a boolean.", penv, env)

exec (SetStmt str exp) penv env =
    let val = eval exp env
        env' = H.insert str val env
    in ("", penv, env')

exec (ProcedureStmt f ps body) penv env = ("", penv', env)
    where penv' = H.insert f (ProcedureStmt f ps body) penv

exec (CallStmt f es) penv env =
    case H.lookup f penv of
        Just (ProcedureStmt f ps body) -> let vs = Prelude.map (\e -> eval e env) es
                                          in exec body penv (H.union env (fromList (Prelude.zip ps vs)))
        Nothing                        -> ("Procedure f undefined", penv, env)
    
exec _ _ _ = fixMe

{-----------------------------------
 - repl
 -----------------------------------}
repl :: PEnv -> Env -> [String] -> String -> IO Result
repl penv env [] _ =
  do putStr "> "
     input <- getLine
     case parse stmt "stdin" input of
        Right QuitStmt -> do putStrLn "Bye!"
                             return ("",penv,env)
        Right x -> let (nuresult,nupenv,nuenv) = exec x penv env
                   in do {
                     putStrLn nuresult;
                     repl nupenv nuenv [] "stdin"
                   }
        Left x -> do putStrLn $ show x
                     repl penv env [] "stdin"

main = do
  putStrLn "Welcome to your interpreter!"
  repl H.empty H.empty [] "stdin"
