package spire

import spire.algebra.Field
import spire.algebra.VectorSpace
import spire.implicits._

object VectorSpaceDemo extends App {
  val vectorSpaceExpr = 5 *: Vector(1, 2, 3) + (-1) *: Vector(3, 4, -5)

  println(vectorSpaceExpr)

  implicit def fnSpace[A] = new VectorSpace[A => Double, Double] {
    implicit def scalar: Field[Double] = Field[Double]

    def zero: A => Double = _ => 0.0
    def negate(x:  A => Double): A => Double = a => -x(a)
    def plus(x:  A => Double, y:  A => Double): A => Double = a => x(a) + y(a)
    def timesl(r:  Double, v:  A => Double): A => Double = a => r * v(a)
  }

  val doubling = (n: Double) => n * 2
  val square = (n: Double) => n * n

  val composed = 4 *: doubling - 3.14 *: square

  println {
    composed(10)
  }
}
