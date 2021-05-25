package cats

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.apply._
import cats.syntax.parallel._

import scala.concurrent.duration.DurationInt

object ConcurrentParallelDemo extends App {
  // Of course, Monads are also applicatives:

//  // Future
//
//  def double(n: Int) = Future {
//    Thread.sleep(1000)
//
//    n * 2
//  }
//
//  def square(n: Int) = Future {
//    Thread.sleep(1000)
//
//    n * n
//  }
//
//  val calc = for {
//    (a, b, c) <- (square(2), square(10), square(20)).tupled
//    d <- double(a + b + c)
//  } yield d
//
//  println(Await.result(calc, 4.seconds))

  // IO

  def double(n: Int): IO[Int] = IO.sleep(1.second) *> IO(n * 2)

  def square(n: Int): IO[Int] = IO.sleep(1.second) *> IO(n * n)

  val calc = for {
    combined <- (square(2), square(10), square(20)).parTupled
    (a, b, c) = combined
    d <- double(a + b + c)
  } yield d

  Utils.time("IO run")(println(calc.unsafeRunSync))
}

object Utils {
  def time[T](name: String)(operation: => T): T = {
    val startTime = System.currentTimeMillis()

    val result = operation

    println(s"$name took ${System.currentTimeMillis - startTime} millis")

    result
  }
}