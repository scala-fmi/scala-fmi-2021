package http

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s._
import org.http4s.implicits._

object Utils {
  def get(uri: Uri): Request[IO] = Request[IO](Method.GET, uri)

  def requestUnsafe(routes: HttpRoutes[IO])(request: Request[IO]): Response[IO] = {
    routes.orNotFound.run(request).unsafeRunSync()
  }
  def requestEntityUnsafe[T](routes: HttpRoutes[IO])(request: Request[IO])(implicit d: EntityDecoder[IO, T]): T = {
    routes.orNotFound.run(request).flatMap(_.as[T]).unsafeRunSync()
  }

  def printResponseAndEntity[T](routes: HttpRoutes[IO])(request: Request[IO])(implicit d: EntityDecoder[IO, T]): IO[Response[IO]] = for {
    resp <- routes.orNotFound.run(request)
    _ <- IO.println(resp)
    _ <- IO.println(resp.as[T])
  } yield resp
}
