package math

object NumericTypeclassDemo extends App {
  // Both use Numeric type class
  List(1, 2, 3, 4).sum
  List(1, 2, 3, 4).product

//   List("a", "b", "cd").sum // does not compile: could not find implicit value for parameter num: Numeric[String]

  implicit val rationalNumeric = new Numeric[Rational] {
    def plus(x: Rational, y: Rational): Rational = x + y
    def minus(x: Rational, y: Rational): Rational = x - y
    def times(x: Rational, y: Rational): Rational = x * y
    def negate(x: Rational): Rational = -x

    def fromInt(x: Int): Rational = x
    def toInt(x: Rational): Int = x.numer / x.denom
    def toLong(x: Rational): Long = x.numer / x.denom
    def toFloat(x: Rational): Float = x.numer.toFloat / x.denom
    def toDouble(x: Rational): Double = x.numer.toDouble / x.denom

    def compare(x: Rational, y: Rational): Int = x compare y

    def parseString(str: String): Option[Rational] = str.split("/") match {
      case Array(nStr, dStr) => for {
        n <- Numeric[Int].parseString(nStr)
        d <- Numeric[Int].parseString(dStr)
      } yield Rational(n, d)
      case _ => None
    }
  }

  List(Rational(3, 4), Rational(1, 2), Rational(2, 5)).sum // 33/20
  List(Rational(3, 4), Rational(1, 2), Rational(2, 5)).product // 3/20
}
