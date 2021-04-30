package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import library.{BookId, Library}
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
  } ~ (get & path("books" / Segment / "name")) { bookIdString =>
    complete(Library.TheGreatLibrary.findBook(BookId(bookIdString)).map {
      case Some(book) => HttpResponse(entity = book.name)
      case None => HttpResponse(StatusCodes.NotFound)
    })
  }

  def main(args: Array[String]): Unit = {
    Http().newServerAt("0.0.0.0", 8080).bindFlow(routes)
  }
}
