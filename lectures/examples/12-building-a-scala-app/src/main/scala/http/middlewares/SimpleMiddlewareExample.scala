package http.middlewares

import cats.data.Kleisli
import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._



object SimpleMiddlewareExample {
  def addHeaderMiddleware(service: HttpRoutes[IO], header: Header.ToRaw): HttpRoutes[IO] = Kleisli { (req: Request[IO]) =>
    service(req).map {
      case Status.Successful(resp) =>
        resp.putHeaders(header)
      case resp =>
        resp
    }
  }

  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "bad" =>
      BadRequest()
    case _ =>
      Ok()
  }

  val wrappedService: HttpRoutes[IO] = addHeaderMiddleware(service, "SomeKey" -> "SomeValue")
}

object SimpleMiddlewareTest extends IOApp {

  import SimpleMiddlewareExample._

  override def run(args: List[String]): IO[ExitCode] = {
    val goodRequest = Request[IO](Method.GET, uri"/")
    val badRequest = Request[IO](Method.GET, uri"/bad")

    for {
      resp1 <- wrappedService.orNotFound(goodRequest)
      _ <- IO.println(resp1)
      resp2 <- wrappedService.orNotFound(badRequest)
      _ <- IO.println(resp2)
    } yield ExitCode.Success

  }
}
