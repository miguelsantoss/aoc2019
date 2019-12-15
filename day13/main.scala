import scala.collection.mutable.ArrayBuffer

class IntCodeInterpreter(var instructions: Array[Long], input: ArrayBuffer[Long], output: ArrayBuffer[Long], var current: Int = 0) {
  var relativeBase = 0
  var halted = false

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
      case 99 => {
        halted = true
        return
      }
    }
    current = nextInstruction
    execute()
  }
}

object Day13 {
  def index(x: Int, y: Int): Int =
    x * 50 + y

  def partOne(instructions: Array[Long]): Unit = {
    val memory = instructions ++ Array.fill[Long](3000)(0)

    var input = ArrayBuffer[Long]()
    var output = ArrayBuffer[Long]()

    val interpreter = new IntCodeInterpreter(memory.clone(), input, output)

    var screen = new Array[Int](50*50)

    interpreter.execute

    output.grouped(3).foreach { vec =>
      screen(index(vec(0).toInt, vec(1).toInt)) = vec(2).toInt
    }

    val answer1 = screen.count(_ == 2)
    println(answer1)
  }

  def printScreen(screen: Array[Int], score: Int): Unit = {
    print("\u001b[2J")
    println()
    print("score: ")
    print(score)
    println()

    for (y <- 0 to 49) {
      for (x <- 0 to 49) {
        screen(index(x, y)) match {
          case 0 => print(" ")
          case 1 => print("|")
          case 2 => print("#")
          case 3 => print("_")
          case 4 => print("O")
        }
      }
      println()
    }
  }

  def partTwo(instructions: Array[Long]): Unit = {
    val memory: Array[Long] = instructions ++ Array(2.toLong) ++ Array.fill[Long](3000)(0)

    var input = ArrayBuffer[Long]()
    var output = ArrayBuffer[Long]()

    val interpreter = new IntCodeInterpreter(memory.clone(), input, output)

    var score = 0
    var screen = new Array[Int](50*50)

    while (!interpreter.halted) {
      interpreter.execute

      output.grouped(3).foreach { vec =>
        (vec(0), vec(1)) match {
          case (-1, 0) => {
            score = vec(2).toInt
          }
          case _ => {
            screen(index(vec(0).toInt, vec(1).toInt)) = vec(2).toInt
          }
        }
      }
      printScreen(screen, score)
    }
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val instructions = lines.split(",").toArray.map(_.trim).filter(_ != "").map(_.toLong)

    partOne(instructions)
    partTwo(instructions)
  }
}
