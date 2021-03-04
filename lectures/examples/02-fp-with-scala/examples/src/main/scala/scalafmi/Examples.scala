package scalafmi

object Examples extends App {
  def toInteger(value: Any): Int = value match {
    case n: Int => n
    case s: String => s.toInt
    case d: Double => d.toInt
  }

  def square(x: Double) = x * x
  square(2)
  toInteger(2)

  toInteger(Array(1, 2, 3))

  def sum(xs: Seq[Int]): Int =
    if (xs.isEmpty) 0
    else xs.head + sum(xs.tail)

  println {
    sum(List.empty)
  }

  def enlist(first: String, rest: List[String]) = (first +: rest).mkString(", ")

  enlist("a", List("b", "c"))
}
