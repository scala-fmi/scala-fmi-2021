package http.client

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val appResource = for {
      client <- BlazeClientBuilder[IO](global).resource

      jokeService = new JokeService(client)
      jokeRouter = new JokeRouter(jokeService)

      server <- BlazeServerBuilder[IO](global)
        .bindHttp(8080, "localhost")
        .withHttpApp(jokeRouter.httpApp)
        .resource
    } yield (client, server)

    appResource.use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
