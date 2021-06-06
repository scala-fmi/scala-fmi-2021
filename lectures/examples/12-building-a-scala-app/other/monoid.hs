class MMonoid a where
  identity :: a
  op :: a -> a -> a
  mmconcat :: [a] -> a
  mmconcat = foldr op identity

newtype SSum a = SSum { getSSum :: a }
  deriving (Eq, Ord, Show)

instance Num a => MMonoid (SSum a) where
  identity = SSum 0
  SSum x `op` SSum y = SSum (x + y)

-- getSSum $ (SSum 5 `op` SSum 8 `op` identity)bn
