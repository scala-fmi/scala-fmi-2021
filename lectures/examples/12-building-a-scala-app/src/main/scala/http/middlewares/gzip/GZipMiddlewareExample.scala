package http.middlewares.gzip

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers.`Accept-Encoding`
import org.http4s.implicits._
import org.http4s.server.middleware._


object GZipMiddlewareExample extends IOApp {
  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case _ =>
      Ok("I repeat myself when I'm under stress. " * 3)
  }

  val zipService = GZip(service)

  override def run(args: List[String]): IO[ExitCode] = {
    val request = Request[IO](Method.GET, uri"/")

    val acceptHeader = `Accept-Encoding`(ContentCoding.gzip)
    val acceptGZipRequest = request.putHeaders(acceptHeader)

    for {
      resp1 <- zipService.orNotFound(request)
      body1 <- resp1.as[String]
      _ <- IO.println(resp1)
      _ <- IO.println(body1)
      resp2 <- zipService.orNotFound(acceptGZipRequest)
      body2 <- resp2.as[String]
      _ <- IO.println(resp2)
      _ <- IO.println(body2)
    } yield ExitCode.Success
  }
}
