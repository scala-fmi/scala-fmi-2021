package streams

import cats.effect._
import fs2.{Pipe, Stream}
import org.http4s._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.websocket.WebSocketBuilder
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.Text

import scala.concurrent.ExecutionContext.global

object Fs205WebSocket extends IOApp.Simple {
  def routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "echo-ws" =>
      val echoReply: Pipe[IO, WebSocketFrame, WebSocketFrame] =
        _.flatMap {
          case Text(msg, _) => Stream(
            Text(s"You sent the server: $msg."),
            Text("Yay :)")
          )
          case _ => Stream(Text("You sent something different than text"))
        }

      WebSocketBuilder[IO].build(echoReply)
  }

  val httpApp = routes.orNotFound

  val serverBuilder = BlazeServerBuilder[IO](global)
    .bindHttp(8080, "localhost")
    .withHttpApp(httpApp)
    .resource

  def run: IO[Unit] = serverBuilder.use(_ => IO.never)
}
