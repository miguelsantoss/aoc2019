object Day08 {
  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString.trim
    // val lines = "0222112222120000"
    val size = 25*6
    val split = lines.grouped(size).toArray

    val answer1 = split.map(s => (s.count(_ == '0'), s.count(_ == '1') * s.count(_ == '2') ))
      .minBy(_._1)

    println(answer1._2)

    val answer2 = 0.until(size).map(i => split.find(l => l(i) != '2').get(i))
    println(answer2.mkString.replace("0", " ").replace("1", "#").grouped(25).mkString("\n"))
  }
}
