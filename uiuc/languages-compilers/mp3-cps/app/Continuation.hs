module Continuation where
import Lib

fixMe = error "fix me!"

repl () =
  do putStr "CPS> "
     input <- getLine
     case parseDecl input of
        Right x -> let result = cpsDecl x
                    in do putStrLn "Pretty Result: "
                          putStrLn $ toStr result
                          putStrLn "Details: "
                          putStrLn $ show result
                          putStrLn ""
                          repl ()
        Left x -> do putStrLn $ show x
                     repl ()

{--------------------------------------
 - Problem 1: factk 
 --------------------------------------}
factk :: Integer -> (Integer -> a) -> a
factk n k | n <= 0    = k $ 1
          | otherwise = factk (n-1) (\r -> k (r * n))
{--------------------------------------
 - Problem 2: evenoddk
 --------------------------------------}
evenoddk :: Integral r => [r] -> (r -> t) -> (r -> t) -> t
evenoddk [x] e o    | even x    = e x
                    | otherwise = o x
evenoddk (x:xs) e o | even x    = evenoddk xs (\v -> e (v+x)) o
                    | otherwise = evenoddk xs e (\v -> o (v+x))

{--------------------------------------
 - Problem 3: isSimple
 --------------------------------------}
isSimple :: Exp -> Bool
isSimple e =
    case e of
       (VarExp _ )      -> True
       (IntExp _ )      -> True
       (OpExp _ e1 e2)  -> isSimple e1 && isSimple e2
       (IfExp e1 e2 e3) -> isSimple e1 && isSimple e2 && isSimple e3
       _                -> False

{--------------------------------------
 - Problem 4: cpsDecl, cpsExp
 --------------------------------------}
cpsDecl :: Stmt -> Stmt
cpsDecl (Decl name vs e) = Decl (name ++ "k") (vs ++ ["k"]) ek
                            where (ek, _ ) = (cpsExp e (VarExp "k") 1) 

cpsExp :: Exp -> Exp -> Integer -> (Exp,Integer)
cpsExp i@(IntExp _) k v = (AppExp k i, v)
cpsExp x@(VarExp _) k v = (AppExp k x, v)

cpsExp g@(AppExp f e) k v | isSimple e = (AppExp g k, v)
                          | otherwise  =  cpsExp e k' v'
                          where (w, v') = gensym v
                                k'      = LamExp w (AppExp (AppExp f (VarExp w)) k)

cpsExp (IfExp c t e) k v | isSimple c = 
                            let (t',  v1)  = cpsExp t k v
                                (e',  v2)  = cpsExp e k v1
                            in  (IfExp c t' e', v2) 
                         | otherwise  =
                            let (w,   v1') = gensym v
                                (t'', v2') = cpsExp t k v1'
                                (e'', v3') = cpsExp e k v2'
                                k'         = LamExp w (IfExp (VarExp w) t'' e'')
                            in cpsExp c k' v3'

cpsExp g@(OpExp f e1 e2) k v | (    s1 &&     s2) = (AppExp k g, v)
                             | (not s1 &&     s2) = cpsExp e1 (LamExp w (AppExp k (OpExp f vw e2))) v1
                             | (    s1 && not s2) = cpsExp e2 (LamExp w (AppExp k (OpExp f e1 vw))) v1
                             | otherwise          = cpsExp e1 (LamExp w k') v3
                             where s1       = isSimple e1
                                   s2       = isSimple e2
                                   (w,  v1) = gensym v
                                   (x,  v2) = gensym v1
                                   vw       = VarExp w
                                   vx       = VarExp x
                                   (k', v3) = cpsExp e2 (LamExp x (AppExp k (OpExp f vw vx))) v2
                                


{--------------------------------------
 - Helper Functions
 --------------------------------------}

gensym :: Integer -> (String,Integer)
gensym i = ("v" ++ show i, i + 1)
