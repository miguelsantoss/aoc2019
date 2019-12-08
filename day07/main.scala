import scala.collection.mutable.ArrayBuffer

class Amplifier(var instructions: Array[Int], input: ArrayBuffer[Int], output: ArrayBuffer[Int], var current: Int = 0) {
  var halted = false

  def execute(): Unit = {
    val opcode = instructions(current) % 100;
    val mode1 = (instructions(current) / 100) % 10;
    val mode2 = (instructions(current) / 1000) % 10;

    lazy val arg1 = if (mode1 == 1) current + 1 else instructions(current + 1)
    lazy val arg2 = if (mode2 == 1) current + 2 else instructions(current + 2)
    lazy val result = instructions(current + 3)

    var nextInstruction = current

    opcode match {
      case 1 => {
        instructions(result) = instructions(arg1) + instructions(arg2)
        nextInstruction += 4
      }
      case 2 => {
        instructions(result) = instructions(arg1) * instructions(arg2)
        nextInstruction += 4
      }
      case 3 => {
        if (input.isEmpty)
          return

        instructions(arg1) = input(0)
        input.remove(0)
        nextInstruction += 2
      }
      case 4 => {
        output += instructions(arg1)
        nextInstruction += 2
      }
      case 5 => {
        nextInstruction =
          if (instructions(arg1) != 0)
            instructions(arg2)
          else
            nextInstruction + 3
      }
      case 6 => {
        nextInstruction =
          if (instructions(arg1) == 0)
            instructions(arg2)
          else
            nextInstruction + 3
      }
      case 7 => {
        instructions(result) =
          if (instructions(arg1) < instructions(arg2)) 1 else 0

        nextInstruction += 4
      }
      case 8 => {
        instructions(result) =
          if (instructions(arg1) == instructions(arg2)) 1 else 0

        nextInstruction += 4
      }
      case 99 => {
        halted = true
        return
      }
    }

    current = nextInstruction

    execute()
  }
}

object Day07 {
  def highestSignal(instructions: Array[Int], min: Int, max: Int): Int = {
    var highestSettings: List[Int] = List()
    var hightest = 0

    for (settings <- List((min to max):_*).permutations) {
      var inputs = Array(
        ArrayBuffer(settings(0), 0),
        ArrayBuffer(settings(1)),
        ArrayBuffer(settings(2)),
        ArrayBuffer(settings(3)),
        ArrayBuffer(settings(4)),
      )

      val amplifiers = Array(
        new Amplifier(instructions.clone(), inputs(0), inputs(1)),
        new Amplifier(instructions.clone(), inputs(1), inputs(2)),
        new Amplifier(instructions.clone(), inputs(2), inputs(3)),
        new Amplifier(instructions.clone(), inputs(3), inputs(4)),
        new Amplifier(instructions.clone(), inputs(4), inputs(0))
      )

      while (!amplifiers.last.halted) {
        amplifiers.foreach(_.execute)
      }

      val temp = inputs(0)(0)

      if (temp > hightest) {
        hightest = temp
        highestSettings = settings
      }
    }

    println(highestSettings.mkString(" "))

    hightest
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val instructions = lines.split(",").toArray.map(_.trim).filter(_ != "").map(_.toInt)

    val answer1 = highestSignal(instructions, 0, 4)
    println(answer1)

    val answer2 = highestSignal(instructions, 5, 9)
    println(answer2)
  }
}
