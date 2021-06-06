package io

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.parallel._

import scala.concurrent.duration.DurationInt

object Ex01RunningIO {
  def double(n: Int): IO[Int] = IO.sleep(1.second) *> IO(n * 2)

  def square(n: Int): IO[Int] = IO.sleep(1.second) *> IO(n * n)

  val calc = for {
    combined <- (square(2), square(10), square(20)).parTupled
    (a, b, c) = combined
    d <- double(a + b + c)
  } yield d

  val calcOutput = calc.timed.flatMap(IO.println)

  def main(args: Array[String]): Unit = {
    calcOutput.unsafeRunSync()

//    calc.unsafeRunAsync(result => println(result))
//    calc.unsafeToFuture().foreach(println)(global.compute)
  }
}
