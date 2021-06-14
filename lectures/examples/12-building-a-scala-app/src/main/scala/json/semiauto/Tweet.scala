package json.semiauto

import io.circe.Codec
import io.circe.parser._
import io.circe.syntax._

case class Tweet(id: Int, content: String, likes: Int = 0)

object Tweet {
  import io.circe.generic.semiauto._

  // or both at the same time
  implicit val tweetCodec: Codec[Tweet] = deriveCodec

}

object DerivedCodecExample extends App {
  val tweet = Tweet(1, "Some random content", 123124)

  println(tweet.asJson)

  val json =
    """
    |{
    |  "id" : 1,
    |  "content" : "Some random content",
    |  "likes" : 123124
    |}""".stripMargin

  val decodedTweet = decode[Tweet](json)
  println(decodedTweet)
}
