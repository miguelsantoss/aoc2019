object Day02 {
  def execute(instructions: Array[Int], current: Int): Unit = {
    instructions(current) match {
      case 1 => handleOp(instructions, current, add)
      case 2 => handleOp(instructions, current, mul)
      case 99 => return
    }

    execute(instructions, current + 4)
  }

  def handleOp(instructions: Array[Int], current: Int, operation: (Int, Int) => Int): Unit = {
    val firstArgPosition = instructions(current + 1)
    val secondArgPosition = instructions(current + 2)
    val resultPosition = instructions(current + 3)

    instructions(resultPosition) = operation(
      instructions(firstArgPosition),
      instructions(secondArgPosition)
    )
  }

  def add(first: Int, second: Int): Int = {
    first + second
  }

  def mul(first: Int, second: Int): Int = {
    first * second
  }

  def findPair(instructions: Array[Int], expected: Int): (Int, Int) = {
    for (i <- 0 to 100) {
      for (j <- 0 to 100) {
        val toRun = instructions.clone()

        toRun(1) = i
        toRun(2) = j

        execute(toRun, 0)

        if (toRun(0) == expected)
          return (i, j)
      }
    }

    (0, 0)
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val instructions = lines.split(",").toArray.map(_.trim).filter(_ != "").map(_.toInt)

    val toRun = instructions.clone()

    toRun(1) = 12
    toRun(2) = 2

    execute(toRun, 0)
    println("first run", toRun(0))

    val solutionPair = findPair(instructions.clone(), 19690720)
    val answer = 100 * solutionPair._1 + solutionPair._2
    println("final", answer)
  }
}
