package cats

import cats.data.{Chain, OptionT}
import cats.instances.all._
import cats.syntax.apply._
import cats.syntax.functor._
import cats.syntax.nested._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Composition extends App {
  val listOfOptions: List[Option[Int]] = List(Some(1), None, Some(2))
  val anotherListOfOptions: List[Option[Int]] = List(None, Some(10), Some(20))

  val ex1 = listOfOptions.map(_.map(_ + 1))
  val ex2 = Functor[List].compose[Option].map(listOfOptions)(_ + 1)

  val ex3 = listOfOptions.map2(anotherListOfOptions)((a, b) => a.map2(b)(_ + _))
  val ex4 = Applicative[List].compose[Option].map2(listOfOptions, anotherListOfOptions)(_ + _)

  println(ex2)
  println(ex4)

  val ex5 = listOfOptions.nested.map(_ + 1).value
  val ex6 = listOfOptions.nested.map2(anotherListOfOptions.nested)(_ + _).value

  println(ex5)

  // Monad composition cannot be defined generically like we did for Functor or Applicative
  // Monads can be composed only if G is a Traverse, i.e. the way composition is done depends on the nested effect
  def composedMonad[F[_], G[_] : Traverse](implicit fm: Monad[F], gm: Monad[G]) =
    new Monad[({type FG[A] = F[G[A]]})#FG] {
      def pure[A](x: A): F[G[A]] = fm.pure(gm.pure(x))

      def flatMap[A, B](fga: F[G[A]])(f: A => F[G[B]]): F[G[B]] = fm.flatMap(fga) { ga =>
        val fApplied = gm.map(ga)(f)
        val fggb = Traverse[G].sequence(fApplied) // we cannot do it if G doesn't support sequence
        fm.map(fggb)(gm.flatten)
      }

      def tailRecM[A, B](a: A)(f: A => F[G[Either[A, B]]]): F[G[B]] = ???
    }

  // OptionT allow us to compose any monad with an Option
  // There is also a variant called EitherT for Eithers
  def monadComposition: Future[Option[String]] = {
    val greetingFO: Future[Option[String]] = Future.successful(Some("Hello"))

    val firstnameF: Future[String] = Future.successful("Jane")

    val lastnameO: Option[String] = Some("Doe")

    val greeting: OptionT[Future, String] = for {
      g <- OptionT(greetingFO)
      f <- OptionT.liftF(firstnameF)
      l <- OptionT.fromOption[Future](lastnameO)
    } yield s"$g $f $l"

    greeting.value
  }
}
