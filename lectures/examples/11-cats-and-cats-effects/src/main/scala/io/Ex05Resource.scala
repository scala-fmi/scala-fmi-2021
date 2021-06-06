package io

import cats.effect.IOApp
import cats.syntax.flatMap._

object Ex05Resource extends IOApp.Simple {
  import cats.effect.{IO, Resource}

  import java.io._

  def inputStream(f: File): Resource[IO, FileInputStream] = Resource.fromAutoCloseable(IO(new FileInputStream(f)))

  def outputStream(f: File): Resource[IO, FileOutputStream] = Resource.fromAutoCloseable(IO(new FileOutputStream(f)))

  def inputOutputStreams(in: File, out: File): Resource[IO, (InputStream, OutputStream)] =
    for {
      inStream  <- inputStream(in)
      outStream <- outputStream(out)
    } yield (inStream, outStream)

  def transmit(origin: InputStream, destination: OutputStream, buffer: Array[Byte], acc: Long): IO[Long] = for {
    amount <- IO.blocking(origin.read(buffer, 0, buffer.length))
    count  <-
      if (amount > -1)
        IO.blocking(destination.write(buffer, 0, amount)) >>
          transmit(origin, destination, buffer, acc + amount)
      else IO.pure(acc)
  } yield count

  def transfer(origin: InputStream, destination: OutputStream): IO[Long] =
    transmit(origin, destination, new Array[Byte](1024 * 10), 0L)

  def copy(origin: File, destination: File): IO[Long] =
    inputOutputStreams(origin, destination).use { case (in, out) =>
      transfer(in, out)
    }

  def run: IO[Unit] =
    copy(new File("build.sbt"), new File("build-copy.sbt")) >>=
      (c => IO.println(s"Transfered $c bytes"))
}
