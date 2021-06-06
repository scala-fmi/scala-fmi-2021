package http.client

import cats.effect.IO
import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

final case class Joke(joke: String) extends AnyVal

object Joke {
  implicit val jokeCodec: Codec[Joke] = deriveCodec[Joke]
}
