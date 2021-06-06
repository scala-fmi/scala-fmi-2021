package io

import cats.effect.{IO, IOApp}
import cats.syntax.flatMap._
import cats.syntax.parallel._

import scala.concurrent.duration.{DurationDouble, DurationInt}

object Ex04Cancellation extends IOApp.Simple {
  def run: IO[Unit] = for {
    printingFiber <- IO.println("La La La... Hello there :)!").foreverM.onCancel(IO.println("Bye :)")).start
    _ <- IO.sleep(2.seconds)
    _ <- printingFiber.cancel
  } yield ()
}

object Ex04Cancellation2 extends IOApp.Simple {
  def run: IO[Unit] = {
    val calc1 = IO.sleep(2.second) *> IO.println("Running calc 1") *> IO.pure(42)
    val calc2 = IO.sleep(1.second) *> IO.println("Running calc 2") *> IO.pure(421)

//    (calc1, calc2).parMapN(_ + _) >>= IO.println
//    (
//      calc1.onCancel(IO.println("calc 1 cancelled")),
//      calc2.onCancel(IO.println("calc 2 cancelled"))
//    ).parMapN(_ + _) >>= IO.println
//    IO.race(calc1.handleErrorWith(_ => IO.never), calc2) >>= IO.println
    IO.race(calc1, IO.sleep(1.seconds)) >>= IO.println // run something and cancel it if it does not finish in time
  }
}
