package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import util.Utils

import scala.concurrent.Future

object FutureWebServer {
  implicit val actorSystem = ActorSystem()
  implicit val ec = actorSystem.dispatcher

  def doWork() = Future {
    Utils.doWork
    Utils.doWork

    42
  }

  val routes: Route = (get & path("do-work")) {
    complete(doWork().map(_.toString))
  }

  def main(args: Array[String]): Unit = {
    val serverBinding = Http().newServerAt("0.0.0.0", 8080).bindFlow(routes)
  }
}
