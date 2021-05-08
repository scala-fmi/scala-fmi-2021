package parallel

import math.Monoid
import math.Monoid.ops._
import parallel.Utils.time

import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ParSeq

object ParallelCollections extends App {
  def sum[A : Monoid](xs: Seq[A]): A = {
    xs.fold(Monoid[A].identity)(_ |+| _)
  }

  def sum[A : Monoid](xs: ParSeq[A]): A = {
    xs.fold(Monoid[A].identity)(_ |+| _)
  }

  val seq = 1 to 900000000

  // Non-associative operation
  implicit val badMonoid = new Monoid[Int] {
    def op(a: Int, b: Int): Int = a - b
    def identity: Int = 0
  }

  // when using the badMonoid the two will produce different results
  println(time("Single threaded")(sum(seq)))
  println(time("Multi threaded")(sum(seq.par)))
}

object Utils {
  def time[T](name: String)(operation: => T): T = {
    val startTime = System.currentTimeMillis()

    val result = operation

    println(s"$name took ${System.currentTimeMillis - startTime} millis")

    result
  }
}
