package streams

import cats.effect.{IO, IOApp}
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.DurationInt

object Fs203Http extends IOApp.Simple {
  val countToTen: Stream[IO, String] =
    Stream.awakeEvery[IO](1.second)
      .map(_.toString + "\n")
      .take(10)

  val counterRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "counter" =>
      Ok(countToTen)
  }

  val httpApp = counterRoutes.orNotFound

  val serverBuilder = BlazeServerBuilder[IO](global)
    .bindHttp(8080, "localhost")
    .withHttpApp(httpApp)
    .resource

  def run: IO[Unit] = serverBuilder.use(_ => IO.never)
}
