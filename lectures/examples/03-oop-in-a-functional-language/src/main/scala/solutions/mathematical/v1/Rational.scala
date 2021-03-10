package solutions.mathematical.v1

class Rational(n: Int, d: Int = 1) {
  require(d != 0)

  val (numer, denom) = {
    val div = gcd(n.abs, d.abs)

    (d.sign * n / div, d.abs / div)
  }

  def unary_- = new Rational(-numer, denom)
  def unary_~ = new Rational(denom, numer)

  def +(that: Rational) = new Rational(
    numer * that.denom + that.numer * denom,
    denom * that.denom
  )

  def -(that: Rational) = this + (-that)

  def *(that: Rational) = new Rational(numer * that.numer, denom * that.denom)

  def /(that: Rational) = this * (~that)

  override def hashCode: Int = (numer, denom).hashCode

  override def equals(obj: Any): Boolean = obj match {
    case that: Rational => numer == that.numer && denom == that.denom
    case _ => false
  }

  override def toString: String = s"$numer/$denom"

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}
