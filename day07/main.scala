import scala.collection.mutable.ArrayBuffer

object Day05 {
  def execute(instructions: Array[Int], input: Array[Int], output: ArrayBuffer[Int], current: Int): Unit = {
    val opcode = instructions(current) % 100;
    val mode1 = (instructions(current) / 100) % 10;
    val mode2 = (instructions(current) / 1000) % 10;

    lazy val arg1 = if (mode1 == 1) current + 1 else instructions(current + 1)
    lazy val arg2 = if (mode2 == 1) current + 2 else instructions(current + 2)
    lazy val result = instructions(current + 3)

    var nextInstruction = current

    opcode match {
      case 1 => {
        nextInstruction += 4
        instructions(result) = instructions(arg1) + instructions(arg2)
      }
      case 2 => {
        nextInstruction += 4
        instructions(result) = instructions(arg1) * instructions(arg2)
      }
      case 3 => {
        nextInstruction += 2
        instructions(arg1) = input(0)
        input.drop(1)
      }
      case 4 => {
        nextInstruction += 2

        output += instructions(arg1)
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
        nextInstruction += 4

        instructions(result) =
          if (instructions(arg1) < instructions(arg2)) 1 else 0
      }
      case 8 => {
        nextInstruction += 4

        instructions(result) =
          if (instructions(arg1) == instructions(arg2)) 1 else 0
      }
      case 99 => return
    }

    execute(instructions, input, output, nextInstruction)
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val instructions = lines.split(",").toArray.map(_.trim).filter(_ != "").map(_.toInt)

    val output1: ArrayBuffer[Int] = ArrayBuffer()
    val toRun1 = instructions.clone()
    execute(toRun1, Array(1), output1, 0)

    val output5: ArrayBuffer[Int] = ArrayBuffer()
    val toRun5 = instructions.clone()
    execute(toRun5, Array(5), output5, 0)

    println(output1.mkString(" "))
    println(output5.mkString(" "))
  }
}
