package http.client

import cats.effect.IO
import org.http4s._
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax

import org.http4s.circe.CirceEntityCodec.circeEntityDecoder

final case class JokeError(e: Throwable) extends RuntimeException

class JokeService(client: Client[IO]) {
  def getJoke: IO[Joke] =
    client.expect[Joke](Request[IO](Method.GET, uri"https://icanhazdadjoke.com/"))
      .handleErrorWith(t => IO.raiseError(JokeError(t)))
}
