package math

import cats.kernel.laws.discipline.MonoidTests
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

class RationalMonoidTest extends AnyFunSuite with FunSuiteDiscipline with Checkers {
  implicit val arbitraryRational: Arbitrary[Rational] = Arbitrary {
    for {
      a <- Gen.choose(-100, 100)
      b <- Gen.choose(-100, 100)
      if b != 0
    } yield Rational(a, b)
  }

  checkAll("Rational Monoid", MonoidTests[Rational].monoid)
}
