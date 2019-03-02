module Unifier where
import qualified Data.HashMap.Strict as H
import Lib

fixMe = error "fix me!"

{---------------------------------------------
 - Given Demo Environments
 -   You might not want to modify this. ;)
 ---------------------------------------------}
phi :: SubstEnv
phi = (H.insert 5 (FnTy BoolTy (TyVar 2)) H.empty)

{---------------------------------------------
 - Problem 1: substFun
 ---------------------------------------------}
substFun :: SubstEnv -> TyCon -> TyCon
substFun env t@(TyVar s) = case H.lookup s env of
   Just tc  -> tc
   _        -> t
   
substFun _ _ = fixMe
   
{---------------------------------------------
 - Problem 2: monoTyLiftSubst
 ---------------------------------------------}
monoTyLiftSubst :: SubstEnv -> TyCon -> TyCon
monoTyLiftSubst env (FnTy a b) = FnTy (monoTyLiftSubst env a) (monoTyLiftSubst env b) 
monoTyLiftSubst env (PairTy a b) = PairTy (monoTyLiftSubst env a) (monoTyLiftSubst env b) 
monoTyLiftSubst env (ListTy a) = ListTy $ monoTyLiftSubst env a
monoTyLiftSubst env t@(TyVar _) = substFun env t
monoTyLiftSubst _ tc = tc

{---------------------------------------------
 - Problem 3: occurs
 ---------------------------------------------}
occurs :: TyCon -> TyCon -> Bool
occurs t (FnTy a b) = (occurs t a) || (occurs t b) 
occurs t (PairTy a b) = (occurs t a) || (occurs t b) 
occurs t (ListTy a) = occurs t a
occurs (TyVar s) (TyVar a) = s == a
occurs _ _ = False

{---------------------------------------------
 - Problem 4: unify
 ---------------------------------------------}
unify :: EqnSet -> Maybe SubstEnv
unify [] = Just H.empty
unify (x@(s,t) : xs)
  | s == t     = unify xs
  | otherwise  = case x of
     tv@(TyVar a, b)          -> elim tv
     (a , b@(TyVar _))        -> unify $ (b,a):xs
     (ListTy a, ListTy b)     -> unify $ (a,b):xs
     (PairTy a b, PairTy c d) -> unify $ (a,c):(b,d):xs
     (FnTy a b, FnTy c d)     -> unify $ (a,c):(b,d):xs
     _ -> Nothing
     where elim (TyVar x, y)
             | occurs (TyVar x) y = Nothing
             | otherwise =
                let e = H.fromList [(x, y)]
                    f    = \(v, w) -> (monoTyLiftSubst e v, monoTyLiftSubst e w)
                in  case unify (map f xs) of
                   Just se -> Just $ H.insert x (monoTyLiftSubst se y) se
                   z       -> z