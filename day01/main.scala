object Day01 {
  def fuel_needed(mass: Int): Int = {
    math.max((mass / 3) - 2, 0)
  }

  def total_fuel_needed(mass: Int): Int = {
    val value = fuel_needed(mass)

    if (value <= 0)
      0
    else
      total_fuel_needed(value) + value
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val modules = lines.split("\n").toSeq.map(_.trim).filter(_ != "").map(_.toInt)

    val initial_fuel = modules.map(x => fuel_needed(x)).sum
    val total_fuel = modules.map(x => total_fuel_needed(x)).sum

    println(initial_fuel)
    println(total_fuel)
  }
}
