package math

object MonoidDemo extends App {
  def sum[A : Monoid](xs: List[A]) = {
    xs.fold(Monoid[A].identity)(_ |+| _)
  }

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
  )) // Some(15/8)
}
