package solutions.mathematical.v2

import scala.language.implicitConversions

class Rational(n: Int, d: Int) extends Ordered[Rational] {
  require(d != 0)

  val (numer, denom) = {
    val div = gcd(n.abs, d.abs)

    (d.sign * n / div, d.abs / div)
  }

  def unary_- = Rational(-numer, denom)

  def unary_~ = Rational(denom, numer)
  def +(that: Rational) = Rational(
    numer * that.denom + that.numer * denom,
    denom * that.denom
  )

  def -(that: Rational) = this + (-that)

  def *(that: Rational) = Rational(numer * that.numer, denom * that.denom)

  def /(that: Rational) = this * (~that)

  override def hashCode: Int = (numer, denom).hashCode

  override def equals(obj: Any): Boolean = obj match {
    case that: Rational => numer == that.numer && denom == that.denom
    case _ => false
  }

  override def toString: String = s"$numer/$denom"

  def compare(that: Rational): Int = (this - that).numer

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}

object Rational {
  def apply(n: Int, d: Int = 1) = new Rational(n, d)

  implicit def intToRational(n: Int): Rational = Rational(n)
}
