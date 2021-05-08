package cats

import math.Rational
import cats.instances.all._
import cats.syntax.eq._

object EqDemo extends App {
  //compiles
  "" == 2

  // won't compile, uses the Eq typeclass
  //  "" === 2

  // compiles
  0 === 2

  implicit val rationalEq = new Eq[Rational] {
    def eqv(x: Rational, y:  Rational): Boolean = x == y
  }

  println(Rational(5) === Rational(10, 2))
  println(Rational(5, 2) =!= Rational(10, 2))
  // doesn't compile
  //  println(Rational(5, 2) === "")
  println(Rational(5, 2) === 2)

  case class Box[+A](a: A) {
    def contains[B >: A : Eq](b: B) = b === a
  }

  Box(1).contains(1)
  // doesn't compile
  // Box(1).contains("")

  // compiles as it doesn't use the type class
  List(1).contains("")
}
