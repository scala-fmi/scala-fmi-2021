package scalafmi

object Substrings extends App {
  def substrings(str: String): Seq[String] = for {
    i <- 0 until str.size
    j <- 1 to str.size - i
  } yield str.drop(i).take(j)

  println {
    substrings("abc")
  }
}
