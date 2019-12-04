object Day04 {
  def incrementingDigits(digits: Seq[Int]): Boolean = {
    var last = -1

    for (digit <- digits) {
      if (digit < last)
        return false

      last = digit
    }

    true
  }

  def adjacentDigits(digits: Seq[Int]): Boolean = {
    for (digit <- digits) {
      val num = digits.filter(_ == digit).length

      if (num >= 2)
        return true
    }

    false
  }

  def digitPair(digits: Seq[Int]): Boolean = {
    for (digit <- digits) {
      val num = digits.filter(_ == digit).length

      if (num == 2)
        return true
    }

    false
  }

  def digits(number: Int): Seq[Int] = {
    if (number == 0)
      return Seq()

    digits(number / 10) ++ Seq(number % 10)
  }

  def main(args: Array[String]): Unit = {
    val min = 278384;
    val max = 824795;

    var guesses1 = Seq[Int]()
    var guesses2 = Seq[Int]()

    for (i <- min to max) {
      val temp = digits(i)

      if (incrementingDigits(temp) && adjacentDigits(temp)) {
        guesses1 = guesses1 :+ i
      }

      if (incrementingDigits(temp) && digitPair(temp)) {
        guesses2 = guesses2 :+ i
      }
    }

    println(guesses1.length)
    println(guesses2.length)
  }
}
