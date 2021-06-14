package fmi.client

import cats.effect.IO
import cats.implicits._
import fmi.BookWithAuthors
import fmi.library._
import io.circe.Decoder
import org.http4s.client.Client

class LibraryApi(client: Client[IO]) {
  import fmi.codecs.LibraryCodecs._
  import org.http4s.circe.CirceEntityCodec._

  private def retrieve[A : Decoder](path: String): IO[A] = client.expect[A](s"http://localhost:8080/$path")

  def listBooks: IO[List[BookSummary]] = retrieve[List[BookSummary]]("books")

  def retrieveAuthor(authorId: AuthorId): IO[Author] = retrieve[Author](s"authors/${authorId.id}")

  def retrieveBookWithAuthors(bookId: BookId): IO[BookWithAuthors] = for {
    book <- retrieve[Book](s"books/${bookId.id}")
    authors <- book.authors.parTraverse(authorId => retrieveAuthor(authorId))
  } yield BookWithAuthors(book, authors)
}
