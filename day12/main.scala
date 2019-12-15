case class Vec3(val x: Int, val y: Int, val z: Int) {
  def plus(v2: Vec3): Vec3 =
    Vec3(x + v2.x, y + v2.y, z + v2.z)

  def diff(v2: Vec3): Vec3 =
    Vec3(-(x compare v2.x), -(y compare v2.y), -(z compare v2.z))

  def sum(): Int =
    math.abs(x) + math.abs(y) + math.abs(z)
}

case class Moon(var position: Vec3, var velocity: Vec3 = Vec3(0, 0, 0)) {
  def energy(): Int =
    position.sum * velocity.sum
}

object Day12 {
  def simulateSteps(moons: Array[Moon], steps: Int): Array[Moon] = {
    val updatedPositions = updateMoons(moons)

    if (steps <= 1)
      return updatedPositions

    simulateSteps(updatedPositions, steps - 1)
  }

  def updateMoons(moons: Array[Moon]): Array[Moon] = {
    val updatedVelocities = moons.map { moon =>
      val newVelocity: Vec3 = moons.filter(_ != moon)
        .foldLeft(moon.velocity) { (acc, m) =>
          val diff = moon.position.diff(m.position)
          acc.plus(diff)
        }

      Moon(moon.position, newVelocity)
    }

    updatedVelocities.map { moon =>
      Moon(moon.position.plus(moon.velocity), moon.velocity)
    }
  }

  def totalEnergy(moons: Array[Moon]): Int = {
    moons.map(moon => moon.energy).sum
  }

  def totalSteps(moons: Array[Moon]): Long = {
    val periods = Array(0, 1, 2).map{ p =>
      period(moons.clone(), moons.clone(), p, 0)
    }

    lcm(periods(0), lcm(periods(1), periods(2)))
  }

  def period(initial: Array[Moon], moons: Array[Moon], dimension: Int, current: Long): Long = {
    if (current > 0 && compareSystem(initial, moons, dimension))
      return current

    period(initial, updateMoons(moons), dimension, current + 1)
  }

  def compareSystem(before: Array[Moon], after: Array[Moon], dimension: Int): Boolean = {
    for (i <- 0 to before.length - 1) {
      if (!compare(before(i), after(i), dimension))
        return false
    }

    true
  }

  def compare(m1: Moon, m2: Moon, dimension: Int): Boolean = {
    dimension match {
      case 0 => m1.position.x == m2.position.x && m1.velocity.x == m2.velocity.x
      case 1 => m1.position.y == m2.position.y && m1.velocity.y == m2.velocity.y
      case 2 => m1.position.z == m2.position.z && m1.velocity.z == m2.velocity.z
      case _ => false
    }
  }

  def gcd(a: Long, b: Long): Long =
    if (b==0) a.abs
    else gcd(b, a % b)

  def lcm(a: Long, b: Long) =
    (a * b).abs / gcd(a, b)

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val pat = """<x=(-?\d+), y=(-?\d+), z=(-?\d+)>""".r
    val moons = lines.split("\n")
      .toArray
      .map(_.trim)
      .filter(_ != "")
      .map(p => pat.findFirstMatchIn(p) match {
        case Some(m) => {
          val position = Vec3(m.group(1).toInt, m.group(2).toInt, m.group(3).toInt)
          Moon(position)
        }
        case None => println("Erron in Moon"); Moon(Vec3(0, 0, 0))
      })

    val simulated = simulateSteps(moons.clone(), 1000)
    val answer1 = totalEnergy(simulated)
    println(answer1)

    val answer2 = totalSteps(moons.clone())
    println(answer2)
  }
}
