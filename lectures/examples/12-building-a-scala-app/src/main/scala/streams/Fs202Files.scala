package streams

import cats.effect.{IO, IOApp}
import fs2.io.file.Files
import fs2.{Stream, text}

import java.nio.file.Paths

object Fs202Files extends IOApp.Simple {
  def converter(inputFile: String, outputFile: String): Stream[IO, Unit] = {
    def fahrenheitToCelsius(f: Double): Double =
      (f - 32.0) * (5.0 / 9.0)

    Files[IO].readAll(Paths.get(inputFile), 4096)
      .through(text.utf8Decode)
      .through(text.lines)
      .filter(s => s.trim.nonEmpty && !s.startsWith("//"))
      .map(line => fahrenheitToCelsius(line.toDouble).toString)
      .intersperse("\n")
      .through(text.utf8Encode)
      .through(Files[IO].writeAll(Paths.get(outputFile)))
  }

  def run: IO[Unit] = converter("fahrenheit.txt", "celsius.txt").compile.drain
}
