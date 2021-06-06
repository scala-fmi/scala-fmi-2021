package http.tweets

import cats.effect.IO
import io.circe.Codec
import org.http4s.{EntityDecoder, EntityEncoder}


case class Tweet(id: Int, content: String, likes: Int = 0)

object Tweet {
  import io.circe.generic.semiauto._
  import org.http4s.circe._

  implicit val tweetCodec: Codec[Tweet] = deriveCodec

  implicit def tweetEntityEncoder: EntityEncoder[IO, Tweet] = jsonEncoderOf[IO, Tweet]
  implicit def tweetsEntityEncoder: EntityEncoder[IO, Seq[Tweet]] = jsonEncoderOf[IO, Seq[Tweet]]
  implicit def tweetEntityDecoder: EntityDecoder[IO, Tweet] = jsonOf[IO, Tweet]
}
