import scala.collection.mutable.ArrayBuffer

case class Point(val x: Int, val y: Int)
case class Panel(val position: Point, val color: Int)

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

object Day11 {
  def turn(current: String, direction: String): String = {
    val directions = Array("Up", "Right", "Down", "Left")
    val curr = directions.indexOf(current)
    val wrap = directions.length - 1

    direction match {
      case "Left" => if (curr > 0) { directions(curr - 1) } else { directions(wrap) }
      case "Right" => if (curr < wrap) { directions(curr + 1) } else { directions(0) }
    }
  }

  def moveForward(position: Point, direction: String): Point = {
    direction match {
      case "Up" => Point(position.x, position.y + 1)
      case "Right" => Point(position.x + 1, position.y)
      case "Down" => Point(position.x, position.y - 1)
      case "Left" => Point(position.x - 1, position.y)
    }
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString
    val instructions = lines.split(",").toArray.map(_.trim).filter(_ != "").map(_.toLong)
    val memory = instructions ++ Array.fill[Long](5000)(0)

    var input = ArrayBuffer[Long]()
    var output = ArrayBuffer[Long]()

    val interpreter = new IntCodeInterpreter(memory.clone(), input, output)

    var panels = new Array[Panel](100*100)
    val index: (Int, Int) => Int = (x, y) => x * 100 + y
    val indexP: (Point) => Int = (p) => index(p.x, p.y)

    for (x <- 0 to 99) {
      for (y <- 0 to 99) {
        panels(index(x,y)) = Panel(Point(x, y), 0)
      }
    }

    var paintedPanels= Array[Point]()
    var currentPosition = Point(50,50)
    var currentDirection = "Up"

    panels(indexP(currentPosition)) = Panel(Point(currentPosition.x, currentPosition.y), 1)

    while (!interpreter.halted) {
      val current = indexP(currentPosition)
      input += panels(current).color

      interpreter.execute()

      if (!interpreter.halted) {
        panels(current) = Panel(panels(current).position, output(0).toInt)
        paintedPanels = paintedPanels :+ panels(current).position

        currentDirection = output(1) match {
          case 0 => turn(currentDirection, "Left")
          case 1 => turn(currentDirection, "Right")
        }

        currentPosition = moveForward(currentPosition, currentDirection)
        output.remove(0)
        output.remove(0)
      }
    }

    // change initial panel color to 0 for answer1
    val answer1 = paintedPanels.toSet.size
    println(answer1)

    val printH: (Panel) => Unit = (p) => if (p.color == 1) { print("#") } else { print(" ") }
    for (y <- 51 to 44 by -1) {
      for (x <- 45 to 99) {
        printH(panels(index(x,y)))
      }
      println
    }
  }
}
