import scala.collection.mutable.ArrayBuffer

class IntCodeInterpreter(var instructions: Array[Long], input: ArrayBuffer[Long], output: ArrayBuffer[Long], var current: Int = 0) {
  var relativeBase = 0

  final def execute(): Unit = {
    val parse: (Int) => (Int, Int, Int, Int) = (instruction) => {
      (instruction % 100, instruction / 100 % 10, instruction / 1000 % 10, instruction / 10000 % 10)
    }

    val arg: (Int, Int) => Int = (mode, argOffset) => {
      lazy val content = instructions(current + argOffset).toInt

      mode match {
        case 1 => current + argOffset
        case 2 => relativeBase + content
        case 0 => content
      }
    }

    val (opcode, mode1, mode2, mode3) = parse(instructions(current).toInt)

    lazy val arg1 = arg(mode1, 1)
    lazy val arg2 = arg(mode2, 2)
    lazy val result = arg(mode3, 3)

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
            instructions(arg2).toInt
          else
            nextInstruction + 3
      }
      case 6 => {
        nextInstruction =
          if (instructions(arg1) == 0)
            instructions(arg2).toInt
          else
            nextInstruction + 3
      }
      case 7 => {
        instructions(result) = if (instructions(arg1) < instructions(arg2)) 1 else 0
        nextInstruction += 4
      }
      case 8 => {
        instructions(result) = if (instructions(arg1) == instructions(arg2)) 1 else 0
        nextInstruction += 4
      }
      case 9 => {
        relativeBase += instructions(arg1).toInt
        nextInstruction += 2
      }
      case 99 => return
    }
    current = nextInstruction
    execute()
  }
}

object Day09 {
  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    // val lines = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
    val instructions = lines.split(",").toArray.map(_.trim).filter(_ != "").map(_.toLong)
    val memory = instructions ++ Array.fill[Long](5000)(0)

    var input = ArrayBuffer[Long](1)
    var output = ArrayBuffer[Long]()

    val interpreter = new IntCodeInterpreter(memory.clone(), input, output)
    interpreter.execute()

    var input2 = ArrayBuffer[Long](2)
    var output2 = ArrayBuffer[Long]()

    val interpreter2 = new IntCodeInterpreter(memory.clone(), input2, output2)
    interpreter2.execute()

    println(output.mkString(" "))
    println(output2.mkString(" "))
  }
}
