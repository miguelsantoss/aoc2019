import sequtils, sugar
import strutils

proc fuelNeeded(mass: int): int =
  max((mass div 3) - 2, 0)

proc recFuelNeeded(mass: int): int =
  var total = 0
  var remaining = mass

  while remaining > 0:
    total += fuelNeeded(remaining)
    remaining = fuelNeeded(remaining)

  total

let input = readFile("input")
let ints = input.split("\n").filter(x => x.len > 0).map(x => parseInt(x))

echo ints.map(x => fuelNeeded(x)).foldl(a + b)
echo ints.map(x => recFuelNeeded(x)).foldl(a + b)
