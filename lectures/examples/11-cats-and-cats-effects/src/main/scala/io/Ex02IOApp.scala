package io

import cats.effect.{ExitCode, IO, IOApp}

object Ex02IOApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    Ex01RunningIO.calcOutput *> IO.pure(ExitCode.Success)
}

object Ex02IOAppSimple extends IOApp.Simple {
  def run: IO[Unit] = Ex01RunningIO.calcOutput
}
