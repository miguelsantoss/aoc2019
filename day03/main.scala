import scala.collection.mutable.ArrayBuffer

case class Point(val x: Int, val y: Int) {
  def toTuple: (Int, Int) =
    (x, y)
}

object Day03 {
  def findClosestDistance(wire1: ArrayBuffer[Point], wire2: ArrayBuffer[Point], initialPoint: Point): Int = {
    var lowestDistance = 0
    val intersections = wire1.intersect(wire2)

    for (point <- intersections) {
      val auxDistance = distance(initialPoint, point)

      if (lowestDistance == 0 || auxDistance < lowestDistance) {
        lowestDistance = auxDistance
      }
    }

    lowestDistance
  }

  def findMinSteps(wire1: ArrayBuffer[Point], wire2: ArrayBuffer[Point], initialPoint: Point): Int = {
    var minSteps = 0
    val intersections = wire1.intersect(wire2)

    for (point <- intersections) {
      val steps = wire1.indexOf(point) + wire2.indexOf(point) + 2

      if (minSteps == 0 || steps < minSteps) {
        minSteps = steps
      }
    }

    minSteps
  }

  def distance(p1: Point, p2: Point): Int = {
    math.abs(p1.x - p2.x) + math.abs(p1.y - p2.y)
  }

  def navigate(positions: ArrayBuffer[Point], directions: Seq[String], current: Int, currentPosition: Point): Unit = {
    if (current >= directions.length)
      return

    val (direction: String, steps: Int) = parseDirection(directions(current))

    val newPosition = direction match {
      case "U" => goUp(positions, currentPosition, steps)
      case "D" => goDown(positions, currentPosition, steps)
      case "L" => goLeft(positions, currentPosition, steps)
      case "R" => goRight(positions, currentPosition, steps)
    }

    navigate(positions, directions, current + 1, newPosition)
  }

  def goUp(positions: ArrayBuffer[Point], currentPosition: Point, steps: Int): Point = {
    val (x, y) = currentPosition.toTuple
    for (i <- y + 1 to y + steps) {
      positions += new Point(x, i)
    }

    new Point(x, y + steps)
  }

  def goLeft(positions: ArrayBuffer[Point], currentPosition: Point, steps: Int): Point = {
    val (x, y) = currentPosition.toTuple
    for (i <- x - 1 to x - steps by -1) {
      positions += new Point(i, y)
    }

    new Point(x - steps, y)
  }

  def goDown(positions: ArrayBuffer[Point], currentPosition: Point, steps: Int): Point = {
    val (x, y) = currentPosition.toTuple
    for (i <- y - 1 to y - steps by -1) {
      positions += new Point(x, i)

    }

    new Point(x, y - steps)
  }

  def goRight(positions: ArrayBuffer[Point], currentPosition: Point, steps: Int): Point = {
    val (x, y) = currentPosition.toTuple
    for (i <- x + 1 to x + steps) {
      positions += new Point(i, y)
    }

    new Point(x + steps, y)
  }

  def parseDirection(direction: String): (String, Int) = {
    val steps = direction.slice(1, direction.length).toInt

    (direction(0).toString, steps)
  }

  def parseWireDirections(wire: String): Seq[String] = {
    wire.split(",").toSeq.map(_.trim).filter(_ != "")
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val wires = lines.split("\n")
      .toSeq
      .map(_.trim)
      .filter(_ != "")
    // val wires = Array(
    //   "R8,U5,L5,D3",
    //   "U7,R6,D4,L4"
    // )
    // val wires = Array(
    //   "R75,D30,R83,U83,L12,D49,R71,U7,L72",
    //   "U62,R66,U55,R34,D71,R55,D58,R83"
    // )
    // val wires = Array(
    //   "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
    //   "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
    // )
    val directions = wires.map(parseWireDirections)

    val initial = new Point(0, 0)
    val wire1: ArrayBuffer[Point] = ArrayBuffer[Point]()
    val wire2: ArrayBuffer[Point] = ArrayBuffer[Point]()

    navigate(wire1, directions(0), 0, initial)
    navigate(wire2, directions(1), 0, initial)

    val shortestDistance = findClosestDistance(wire1, wire2, initial)
    val minSteps = findMinSteps(wire1, wire2, initial)

    println(shortestDistance)
    println(minSteps)
  }
}
