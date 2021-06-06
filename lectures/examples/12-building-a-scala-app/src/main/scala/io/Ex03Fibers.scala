package io

import cats.effect.{IO, IOApp}
import cats.syntax.parallel._
import cats.syntax.flatMap._

import scala.Function.tupled

object Ex03Fibers extends IOApp.Simple {
  def run: IO[Unit] = {
    val fibers = for {
      fiber1 <- Ex01RunningIO.double(10).start
      fiber2 <- Ex01RunningIO.square(10).start

      a <- fiber1.joinWithNever
      b <- fiber2.joinWithNever
    } yield a + b

    fibers.timed.flatMap(IO.println)
  }
}

object Ex03FibersBetter extends IOApp.Simple {
  def run: IO[Unit] = {
    val result = IO.both(
      Ex01RunningIO.double(10),
      Ex01RunningIO.square(10)
    ).map(tupled(_ + _))

    result.timed.flatMap(IO.println)
  }
}

object Ex03FibersBest extends IOApp.Simple {
  def run: IO[Unit] = {
    val result = (
      Ex01RunningIO.double(10),
      Ex01RunningIO.square(10)
    ).parMapN(_ + _)

//    result.timed.flatMap(IO.println)
    result.timed >>= IO.println
  }
}