module Mp1 where

data Cons a = Cons a (Cons a)
            | Nil
  deriving (Show,Eq)

data Exp = IntExp Int
         | PlusExp [Exp]
         | MultExp [Exp]
  deriving (Show,Eq)

{-
 - You'll want to go through and put in the type signatures first, if you want
 - to check your work incrementally with quickCheck. (quickCheck is a way to
 - create random tests to validate your work.)
 -
 - To use our quickChecks, you'll want to load up mp1check by going:
 -   :l mp1check
 -
 - then for each property in mp1check, you'll want to test with:
 -   > quickCheck PROP_NAME
 -
 - For example, to test mytake, you would run:
 -   > quickCheck prop_mytake
 -}

mytake :: Int -> [Int] -> [Int]
mytake _ [] = []
mytake 0 _  = []
mytake n (x:xs) | n < 0     = []
                | otherwise = x : mytake (n-1) xs

mydrop :: Int -> [Int] -> [Int]
mydrop _ [] = []
mydrop 0 xx = xx
mydrop n (x:xs) | n < 0     = x:xs
                | otherwise = mydrop (n-1) xs

rev :: [Int] -> [Int]
rev xx = aux xx []
    where aux []     acc = acc
          aux (x:xs) acc = aux xs (x:acc)

app :: [Int] -> [Int] -> [Int]
app xx [] = xx
app [] yy = yy
app (x:xs) yy = x : app xs yy

add :: Ord a => a -> [a] -> [a]
add a [] = [a]
add a (x:xs) | a == x    = x:xs
             | a < x     = a:x:xs
             | otherwise = x : add a xs

union :: Ord a => [a] -> [a] -> [a]
union xx [] = xx
union [] yy = yy
union (x:xs) (y:ys) | x < y     = x : union xs (y:ys)
                    | x > y     = y : union (x:xs) ys
                    | otherwise = x : union xs ys

intersect :: Ord a => [a] -> [a] -> [a]
intersect _ [] = []
intersect [] _ = []
intersect (x:xs) (y:ys) | x == y    = x : intersect xs ys
                        | x >  y    =     intersect (x:xs) ys
                        | otherwise =     intersect xs (y:ys)

powerset :: Ord a => [a] -> [[a]]
powerset [] = [[]]
powerset (x:xs) = union ps (map (x:) ps)
    where ps = powerset xs

inclist :: (Num a) => [a] -> [a]
inclist [] = []
inclist (x:xs) = (x+1) : inclist xs

sumlist :: (Num t) => [t] -> t
sumlist xx = aux xx 0
    where aux [] acc = acc
          aux (x:xs) acc = aux xs (x+acc)

myzip :: [t] -> [t1] -> [(t, t1)]
myzip xx [] = []
myzip [] yy = []
myzip (x:xs) (y:ys) = (x,y) : myzip xs ys

addpairs :: (Num a) => [a] -> [a] -> [a]
addpairs xx yy = aux (myzip xx yy)
    where aux [] = []
          aux (x:xs) = (fst x + snd x) : aux xs

ones :: [Integer]
ones = 1 : ones

nats :: [Integer]
nats = 1 : map (+ 1) nats

fib :: [Integer]
fib = 1 : 1 : addpairs fib (tail fib)

list2cons :: [a] -> Cons a
list2cons [] = Nil
list2cons (x:xs) = Cons x (list2cons xs)

cons2list :: Cons a -> [a]
cons2list Nil = []
cons2list (Cons x y) = x : cons2list y

eval :: Exp -> Int
eval (IntExp i) = i
eval (PlusExp xx) = foldr (+) 0 (map eval xx)
eval (MultExp xx) = foldl (*) 1 (map eval xx)

inclist' :: (Num a) => [a] -> [a]
inclist' [] = []
inclist' xx = map (+1) xx

sumlist' :: (Num t) => [t] -> t
sumlist' = foldr (+) 0

list2cons' :: [a] -> Cons a
list2cons' = foldr (Cons) Nil
