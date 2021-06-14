package fmi.client

import cats.effect.IO
import cats.implicits._
import fmi.BookWithAuthors
import fmi.library.{BookId, BookSummary}
import org.http4s.Status.NotFound
import org.http4s.client.UnexpectedStatus

import scala.util.control.NonFatal

class LibraryClientUI(libraryApi: LibraryApi) {
  def selectBook: IO[Unit] = (for {
    books <- libraryApi.listBooks
    _ <- IO.println("Available books: ")
    _ <- displayBooks(books)

    bookId <- promptInput("Select book id:").map(BookId)
    _ <- retrieveAndDisplayBook(bookId)

    anotherBookInput <- promptInput("Select another book?")
    _ <-
      if (anotherBookInput == "y") selectBook
      else IO.unit
  } yield ()) recoverWith {
    case NonFatal(_) => IO.println("Something unexpected went wrong. See you again!")
  }

  def displayBooks(books: List[BookSummary]): IO[Unit] = {
    // No need for parallelism here so we can use traverse instead of parTraverse
    // Otherwise books will be printed in unpredictable order
    books.map(b => s"ID: ${b.id.id}, name: ${b.name}").traverse(IO.println).void // void turns the result into IO[Unit]
  }

  def retrieveAndDisplayBook(bookId: BookId): IO[Unit] = {
    (for {
      bookWithAuthors <- libraryApi.retrieveBookWithAuthors(bookId)
      _ <- displayBook(bookWithAuthors)
    } yield ()) recoverWith {
      case UnexpectedStatus(NotFound, _, _) => IO.println("Book not found")
    }
  }

  def displayBook(bookWithAuthours: BookWithAuthors) = for {
    _ <- IO.println("Information about selected book:")

    BookWithAuthors(book, authors) = bookWithAuthours

    _ <- IO.println(s"Name: ${book.name}")
    _ <- IO.println(s"Genre: ${book.genre}")
    _ <- IO.println(s"Authors: ${authors.map(_.name).mkString(", ")}")
  } yield ()

  def promptInput(prompt: String): IO[String] = for {
    _ <- IO.println(prompt)
    input <- IO.readLine
  } yield input
}
