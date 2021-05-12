package exersices.effects

sealed trait Maybe[+A]
case class Just[+A](a: A) extends Maybe[A]
case object Nthng extends Maybe[Nothing]

object Maybe {
  implicit val maybeMonad = new Monad[Maybe] {
    def flatMap[A, B](fa:  Maybe[A])(f: A => Maybe[B]): Maybe[B] = fa match {
      case Just(a) => f(a)
      case _ => Nthng
    }

    def unit[A](a:  =>A): Maybe[A] = Just(a)
  }
}

object MaybeTest extends App {
  import Monad.ops._

  val m: Maybe[Int] = Just(1)

}
