case class Point(val x: Int, val y: Int)

object Day10 {
  def radius(center: Point, point: Point) = {
    math.atan2(point.y - center.y, point.x - center.x)
  }

  def distance(p1: Point, p2: Point): Int = {
    math.abs(p1.x - p2.x) + math.abs(p1.y - p2.y)
  }

  def countVisible(position: Point, asteroids: Array[Point]) = {
    asteroids.filter(_ != position).map(at => radius(position, at)).toSet.size
  }

  def destroyAsteroids(position: Point, asteroids: Array[Point], count: Int): Point = {
    if (asteroids.isEmpty)
      return Point(0,0)

    val closest = asteroids.filter(_ != position)
      .map(at => (at, distance(position, at), radius(position, at)))
      .groupBy(_._3)
      .map {
        case (k, v) => {
          val min = v.minBy(_._2)
          (min._1, min._3)
        }
      }
      .toArray
      .sortBy(_._2)

    val nextCount = count + closest.size
    if (nextCount > 200) {
      val offset = 200 - count
      val start = closest.indexWhere(x => x._2 >= math.toRadians(-90))
      val index = start + offset
      val px = if (index > closest.size - 1) index - closest.size - 1 else index

      return closest(px)._1
    }

    val remaining = asteroids.diff(closest.map(_._1))
    destroyAsteroids(position, remaining, nextCount)
  }


  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val map = lines.split("\n").toArray.map(_.trim).filter(_ != "")

    val height = map.size
    val width = map(0).size

    var positions: Array[Point] = Array[Point]()

    for (y <- 0 to map.length - 1) {
      for (x <- 0 to map(y).length - 1) {
        if (map(y)(x) == '#')
          positions = positions :+ Point(x, y)
      }
    }

    val bestLocation = positions.maxBy(countVisible(_, positions))
    val answer1 = countVisible(bestLocation, positions)

    val last = destroyAsteroids(bestLocation, positions.filter(_ != bestLocation), 0)
    val answer2 = last.x * 100 + last.y

    println(answer1)
    println(answer2)
  }
}
