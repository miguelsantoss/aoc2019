fuelCost :: Int -> Int
fuelCost x = max 0 $ subtract 2 $ div x 3

recFuelCost :: Int -> Int
recFuelCost = sum . takeWhile (> 0) . tail . iterate fuelCost

readInts :: FilePath -> IO[Int]
readInts file = map toInt . lines <$> readFile file
  where
    toInt :: String -> Int
    toInt = read

main :: IO ()
main = do
  input <- readInts "input"
  print (sum (map fuelCost input))
  print (sum (map recFuelCost input))
