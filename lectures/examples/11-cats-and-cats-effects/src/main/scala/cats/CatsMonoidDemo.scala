package cats

import cats.arrow.FunctionK
import cats.data.{Nested, Validated}
import math.Rational
import cats.instances.all._

import scala.concurrent.Future
// We are importing only the syntax we are going to use
import cats.syntax.apply._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.monad._
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.syntax.validated._
import cats.syntax.either._
import cats.syntax.option._
import cats.syntax.functor._
import cats.syntax.parallel._
import cats.syntax.monoid._
import cats.~>

import scala.concurrent.ExecutionContext.Implicits.global

object CatsMonoidDemo extends App {
  Parallel
//  Apply[List].map()
//  Applicative[List].map()
//  Traverse[List].map()


//  val a2: Parallel[Option] = ???

  "asdfds".asRight

  Traverse


  (Option(1), Option(2), Option(3)).mapN((x, y, z) => x + y + z)
//  (Option(1), Option(1), Option(1)).parMapN
//  List(true, true, false).ifA()

  println {
    (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)).mapN((x, y, z) => x + y + z)
  }

  println {
    (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)).parMapN((x, y, z) => x + y + z)
  }

  (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)).sequence

  val a = (a: Int, b: String) => b * a

  Monoid[Int].leftNec

//  Nested[List, Option, Int](List(None)).map()

  val b = a.curried.pure[Option] <*> 2.some <*> "3".some

//  cats.data.

  2.validNec

//  Applicative[Option].compose
//  (2, 3) |+| (4, 5)
//
//  implicit val rationalMonoid = new Monoid[Rational] {
//    def empty: Rational = 0
//    def combine(x: Rational, y: Rational): Rational = x + y
//  }
//
//  val map1 = Map(1 -> (2, Rational(3, 2)), 2 -> (3, Rational(4)))
//  val map2 = Map(2 -> (5, Rational(6)), 3 -> (7, Rational(8, 3)))
//
//  println(map1 |+| map2)
}
