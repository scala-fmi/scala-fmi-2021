package effects.cats

import cats.{Monad, MonadError}
import effects.maybe.{Just, Maybe, Nthng}

object MonadExample extends App {
    implicit val maybeMonad: Monad[Maybe] = new Monad[Maybe] {
      override def pure[A](x: A): Maybe[A] = Just(x)

      override def flatMap[A, B](fa: Maybe[A])(f: A => Maybe[B]): Maybe[B] = fa match {
        case Just(a) => f(a)
        case _ => Nthng
      }

      /*
      Needed from Cats to do stack safe monadic recursion
      more info [here](https://typelevel.org/cats/typeclasses/monad.html#tailrecm)
       */
      override def tailRecM[A, B](a: A)(f: A => Maybe[Either[A, B]]): Maybe[B] = f(a) match {
        case Nthng              => Nthng
        case Just(Left(nextA)) => tailRecM(nextA)(f) // continue the recursion
        case Just(Right(b))    => Just(b)            // recursion done
      }
    }

  import cats.implicits._

  def f(n: Int): Maybe[Int] = Just(n + 1)
  def g(n: Int): Maybe[Int] = Just(n * 2)
  def h(n: Int): Maybe[Int] = Just(n * n)

  val a = 3

  val result = for {
    b <- f(a)
    c <- g(b * 2)
    d <- h(b + c)
  } yield a * b * d
  println(result)
}
