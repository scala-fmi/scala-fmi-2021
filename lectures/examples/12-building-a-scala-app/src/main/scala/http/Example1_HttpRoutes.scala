package http

import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.{HttpApp, HttpRoutes, Request, Response, Status}
import org.http4s.dsl.io._
import org.http4s.implicits._
import cats.syntax.semigroupk._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router

import scala.concurrent.ExecutionContext.global

object Example1_HttpRoutes {

  val mostSimpleRoute = HttpRoutes.of[IO] {
    case _ =>
      IO(Response(Status.Ok))
  }

  val helloRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>  //   GET /hello/zdravko
      Ok(s"Hello, $name.")
    case GET -> Root / "hola" / name =>
      Ok(s"¡Hola, $name!")
  }

  val combined = helloRoutes <+> mostSimpleRoute

  // Or define them under different path prefixes like that
  Router("/" -> helloRoutes, "/simple" -> mostSimpleRoute)

  val httpApp: HttpApp[IO] = helloRoutes.orNotFound
}

object HelloWorldApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(Example1_HttpRoutes.httpApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}


