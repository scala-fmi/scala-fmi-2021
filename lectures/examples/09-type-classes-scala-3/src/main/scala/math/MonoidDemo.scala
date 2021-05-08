package math

import Monoid.given
// Or specify just the givens you are going to use:
// import Monoid.{ given Monoid[Int], given Monoid[(_, _)] }

object MonoidDemo extends App {
  def sum[A](xs: List[A])(using Monoid[A]) = {
    xs.fold(Monoid[A].identity)(_ |+| _)
  }

  sum(List(Rational(3, 4), Rational(5), Rational(7, 4), Rational(11, 13)))
  sum(List(Rational(1, 2), Rational(4)))
  sum(List(1, 2, 3, 4, 5))
  sum(List(1, 2, 3, 4, 5))(using Monoid.intMultiplicativeMonoid)

  println {
    given Monoid[Int] = Monoid.intMultiplicativeMonoid

    sum(List(1, 2, 3, 4, 5))
  }

  sum(List(
    Some(Rational(1)),
    None,
    Some(Rational(1, 2)),
    Some(Rational(3, 8)),
    None
  ))

  (1, 2) |+| (3, 4)
}
