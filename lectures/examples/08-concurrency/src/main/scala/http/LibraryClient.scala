package http

import concurrent.ExecutionContexts
import concurrent.ExecutionContexts.default
import console.Console

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.control.NonFatal

case class Author(name: String)
case class Book(name: String, authors: List[Author], genre: String)

case class BookSummary(id: String, name: String)

object LibraryApi {
  private def retrieve(path: String) =
    HttpClient.getScalaFuture(s"http://localhost:8080/$path").flatMap { response =>
      if (response.getStatusCode == 200) Future.successful(response.getResponseBody)
      else Future.failed(BadResponse(response.getStatusCode))
    }

  def retrieveBookSummary(bookId: String): Future[BookSummary] =
    retrieve(s"books/$bookId/name")
      .map(BookSummary(bookId, _))

  def listBooks: Future[List[BookSummary]] = for {
    bookIds <- retrieve("books").map(asList)
    books <- Future.sequence(
      bookIds.map(retrieveBookSummary)
    )
  } yield books

  def retrieveAuthor(authorId: String): Future[Author] = for {
    name <- retrieve(s"authors/$authorId/name")
  } yield Author(name)

  def retrieveBook(bookId: String): Future[Book] = for {
    ((name, genre), authorIds) <-
      retrieve(s"books/$bookId/name") zip
      retrieve(s"books/$bookId/genre") zip
      retrieve(s"books/$bookId/authors").map(asList)
    authors <- Future.sequence(
      authorIds.map(retrieveAuthor)
    )
  } yield Book(name, authors, genre)

  private def asList(elementsString: String): List[String] = elementsString.split(",").toList
}

object LibraryClient extends App {
  val console = new Console(ExecutionContexts.blocking)

  def selectBook: Future[Unit] = for {
    books <- LibraryApi.listBooks
    _ <- console.putStringLine("Available books: ")
    _ <- displayBooks(books)

    bookId <- promptInput("Select book id:")
    _ <- retrieveAndDisplayBook(bookId)

    anotherBookInput <- promptInput("Select another book?")
    _ <-
      if (anotherBookInput == "y") selectBook
      else Future.successful(())
  } yield ()

  def displayBooks(books: List[BookSummary]): Future[Unit] = Future.sequence(
    books.map(b => s"ID: ${b.id}, name: ${b.name}").map(console.putStringLine)
  ).map(_ => ())

  def retrieveAndDisplayBook(bookId: String): Future[Unit] = {
    (for {
      book <- LibraryApi.retrieveBook(bookId)
      _ <- displayBook(book)
    } yield ()) recoverWith {
      case BadResponse(404) => console.putStringLine("Book not found")

      // NonFatal.unapply helps us not to catch JVM errors and other exceptions
      // that we cannot recover from and thus should never be caught
      case NonFatal(_) => console.putStringLine("Something went wrong")
    }
  }

  def displayBook(book: Book) = for {
    _ <- console.putStringLine("Information about selected book:")
    _ <- console.putStringLine(s"Name: ${book.name}")
    _ <- console.putStringLine(s"Genre: ${book.genre}")
    _ <- console.putStringLine(s"Authors: ${book.authors.map(_.name).mkString(", ")}")
  } yield ()

  def promptInput(prompt: String): Future[String] = for {
    _ <- console.putStringLine(prompt)
    input <- console.getStringLine
  } yield input

  Await.result(selectBook, Duration.Inf)
}
