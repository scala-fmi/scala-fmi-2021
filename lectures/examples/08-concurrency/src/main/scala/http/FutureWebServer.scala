package http

import java.util.NoSuchElementException
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import library.{BookId, AuthorId, Library}
import util.Utils
import Library.TheGreatLibrary

import scala.concurrent.Future

object FutureWebServer {
  implicit val actorSystem = ActorSystem()
  implicit val ec = actorSystem.dispatcher

  def doWork = Future {
    Utils.doWork
    Utils.doWork

    42
  }

  val routes = (get & path("do-work")) {
    complete(doWork.map(_.toString))
  } ~ get {
    library
  }

  def library = findBookName ~ findBookAuthors ~ findAuthorName

  def findBookName =
    path("books" / Segment / "name") { bookIdString =>
      val bookId = BookId(bookIdString)

      complete {
        Library.TheGreatLibrary.findBook(bookId).map {
          case Some(book) => HttpResponse(entity = book.name)
          case None => HttpResponse(StatusCodes.NotFound)
        }
      }
    }

  def findBookAuthors =
    path("books" / Segment / "authors") { bookIdString =>
      val bookId = BookId(bookIdString)

      complete {
        Library.TheGreatLibrary.findBook(bookId).map {
          case Some(book) => HttpResponse(entity = book.authors.map(_.id).mkString(","))
          case None => HttpResponse(StatusCodes.NotFound)
        }
      }
    }

  def findAuthorName =
    path("authors" / Segment / "name") { authorIdString =>
      val authorId = AuthorId(authorIdString)

      complete {
        TheGreatLibrary.findAuthor(authorId).map {
          case Some(author) => HttpResponse(entity = author.name)
          case None => HttpResponse(StatusCodes.NotFound)
        }
      }
    }

  def toEntityResponse(maybeEntity: Option[String]) = maybeEntity match {
    case Some(entity) => HttpResponse(entity = entity)
    case None => HttpResponse(StatusCodes.NotFound)
  }

  def main(args: Array[String]): Unit = {
    val serverBinding = Http().newServerAt("0.0.0.0", 8080).bindFlow(routes)
  }
}
