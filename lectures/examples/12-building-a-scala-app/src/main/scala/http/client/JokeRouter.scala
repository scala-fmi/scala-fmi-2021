package http.client

import cats.effect.IO
import io.circe.Json
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.jsonDecoder
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.dsl.io._
import org.http4s.implicits._

class JokeRouter(jokeService: JokeService) {
  def jokeRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "joke" =>
      jokeService.getJoke.flatMap(Ok(_))
    case req =>
      req.as[Json].flatMap(Ok(_))
  }

  def httpApp: HttpApp[IO] = jokeRoutes.orNotFound
}
