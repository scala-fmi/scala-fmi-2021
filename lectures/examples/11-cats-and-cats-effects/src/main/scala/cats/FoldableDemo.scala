package cats

import cats.syntax.foldable._

object FoldableDemo extends App {
  def doSomething[F[_] : Foldable, A : Monoid](f: F[A]): A = {
    f.forall(_ != Monoid[A].empty)

    f.foldMap(_.toString)

    f.combineAll
  }

  doSomething(List(10, 20, 30, 42))

}
