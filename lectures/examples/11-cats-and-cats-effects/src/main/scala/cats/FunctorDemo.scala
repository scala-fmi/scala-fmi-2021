package cats

import cats.instances.all._
import cats.syntax.functor._

object FunctorDemo extends App {
  val ex1 = Option(("a", 0)).swapF
  val ex2 = List(("a", 0), ("b", 1), ("c", 2)).swapF.toMap

  def genericDouble[F[_] : Functor](ints: F[Int]): F[Int] = ints.map(_ * 2)

  println(ex1)
  println(ex2)
  println(genericDouble(Option(10)))
  println(genericDouble(List(10, 20, 30)))
}
