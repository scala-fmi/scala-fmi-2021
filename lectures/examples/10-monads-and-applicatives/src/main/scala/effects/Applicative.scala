package effects

import scala.util.Try

trait Applicative[F[_]] extends Functor[F] {

  // primitive combinators
  def map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
  def unit[A](a: => A): F[A]

  // derived combinators
  def map[A, B](fa: F[A])(f: A => B): F[B] = map2(fa, unit(()))((a, _) => f(a))

  def apply[A, B](fab: F[A => B])(fa: F[A]): F[B] = map2(fab, fa)(_(_))

  def zip[A,B](fa: F[A], fb: F[B]): F[(A,B)] =
    map2(fa, fb)((_,_))

  def map3[A,B,C,D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] = {
    val product = zip(zip(fa, fb), fc)
    map(product) {
      case ((a, b), c) => f(a, b, c)
    }
  }

  /*
  The pattern is simple. We just curry the function
  we want to lift, pass the result to `unit`, and then `apply`
  as many times as there are arguments.
  Each call to `apply` is a partial application of the function
  */
  def map4[A,B,C,D,E](fa: F[A],
                      fb: F[B],
                      fc: F[C],
                      fd: F[D])(f: (A, B, C, D) => E): F[E] =
    apply(apply(apply(apply(unit(f.curried))(fa))(fb))(fc))(fd)

  def sequence[A](lfa: List[F[A]]): F[List[A]] =
    traverse(lfa)(fa => fa)

  def traverse[A, B](as: List[A])(f: A => F[B]): F[List[B]] =
    as.foldRight(unit(List[B]()))((a, mbs) => map2(f(a), mbs)(_ :: _))

}


object Applicative {
  def apply[F[_]](implicit a: Applicative[F]) = a

  object ops {
    implicit class ListOfEffectsWithApplicativeOps[F[_] : Applicative, A](lfa: List[F[A]]) {
      def sequence = Applicative[F].sequence(lfa)
    }
    implicit class ListWithApplicativeOps[F[_]: Applicative, A](la: List[A]) {
      def traverse[B](f: A => F[B]) = Applicative[F].traverse(la)(f)
    }
  }

  implicit val optionApplicative = new Applicative[Option] {
    def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = (fa, fb) match {
      case (Some(a), Some(b)) => Some(f(a, b))
      case (_, _) => None
    }

    def unit[A](a: => A): Option[A] = Some(a)
  }

  implicit def eitherApplicative[L] = {
    new Applicative[({ type E[R] = Either[L, R] })#E] {
      def map2[A, B, C](fa: Either[L, A], fb: Either[L, B])(f: (A, B) => C): Either[L, C] = (fa, fb) match {
        case (Right(a), Right(b)) => Right(f(a, b))
        case (Left(l) , _       ) => Left(l)
        case (_       , Left(l) ) => Left(l)
      }

      def unit[A](a: => A): Either[L, A] = Right(a)
    }
  }
}

object ApplicativeDemo extends App {

  import Applicative._

  val o1 = Some(1)
  val o2 = None
  val o3 = Some(3)
  val o4 = Some(4)

  println{
    optionApplicative.map4(o1, o2, o3, o4){ case tuple4 => tuple4 }
  }

  println{
    optionApplicative.map3(o1, o3, o4){ case tuple3 => tuple3 }
  }
}

object ApplicativeSequenceDemo extends App {
  import Applicative._

  val listOfOptions: List[Option[Int]] = (1 to 10).map(Some.apply).toList
  val listOfOptionsWithNone: List[Option[Int]] = List(Some(1), None, Some(3))

  println {
    Applicative[Option].sequence(listOfOptions)
  }
  println {
    Applicative[Option].sequence(listOfOptionsWithNone)
  }

  type EitherString[T] = Either[String, T]

  val listOfEithers: List[EitherString[Int]] = List(Right(1), Right(2), Right(3))
  val listOfEithersWithLeft: List[EitherString[Int]] = List(Left("one"), Right(2), Left("three"))

  println {
    Applicative[EitherString].sequence(listOfEithers)
  }
  println {
    Applicative[EitherString].sequence(listOfEithersWithLeft)
  }
}

object ApplicativeTraverseDemo extends App {

  import Applicative._
  import Applicative.ops._

  type EitherNFE[T] = Either[NumberFormatException, T]

  def parseIntEither(s: String): EitherNFE[Int] =
    try Right(s.toInt)
    catch {
      case ex: NumberFormatException => Left(ex)
    }

  println {
    Applicative[EitherNFE].traverse(List("1", "2", "3"))(parseIntEither)
  }

  println {
    Applicative[EitherNFE].traverse(List("1", "abc", "3"))(parseIntEither)
  }
}
