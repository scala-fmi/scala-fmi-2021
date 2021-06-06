package http.tweets

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

object Example5_EntityEncoders {

  object TweetService {
    def getTweet(tweetId: Int): IO[Tweet] = IO.pure(Tweet(tweetId, s"tweet with id $tweetId"))
    def getPopularTweets(): IO[Seq[Tweet]] = IO.pure(
      Seq(
        Tweet(1, s"tweet 1", 10),
        Tweet(2, s"tweet 2", 13),
        Tweet(3, s"tweet 3", 8),
      )
    )
    def createTweet(tweet: Tweet): IO[Tweet] = for {
        _ <- IO.sleep(100.millis)
        _ <- IO.println(s"Creating: $tweet")
      } yield tweet
  }

//  import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
//  import org.http4s.circe.CirceEntityCodec.circeEntityDecoder

  val tweetRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / IntVar(tweetId) =>
      TweetService.getTweet(tweetId).flatMap(Ok(_))
    case GET -> Root / "tweets" / "popular" =>
      TweetService.getPopularTweets().flatMap(Ok(_))
    case req @ POST -> Root / "tweets" =>
      req.as[Tweet]
        .flatMap(TweetService.createTweet)
        .flatMap(Created(_))
    case req @ POST -> Root / "tweets" / "errorhandling" =>
      req.attemptAs[Tweet].foldF(
        _ => BadRequest("invalid tweet"),
        tweet =>
          TweetService.createTweet(tweet).flatMap(Created(_))
      )
  }

  val httpApp = tweetRoutes.orNotFound
}

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(Example5_EntityEncoders.httpApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
