package library

import scala.concurrent.Future

case class BookId(id: String) extends AnyVal
case class Book(id: BookId, name: String, authors: List[AuthorId], genre: String)

case class AuthorId(id: String) extends AnyVal
case class Author(id: AuthorId, name: String)

class Library(books: List[Book], authors: List[Author]) {
  private val bookIdToBook = books.map(book => book.id -> book).toMap
  private val authorIdToAuthor = authors.map(author => author.id -> author).toMap

  def findBook(bookId: BookId): Future[Option[Book]] = Future.successful(bookIdToBook.get(bookId))

  def findAuthor(authorId: AuthorId): Future[Option[Author]] = Future.successful(authorIdToAuthor.get(authorId))
}

object Library {
  private val books = List(
    Book(BookId("1"), "Programming in Scala", List(AuthorId("1"), AuthorId("2")), "Computer Science")
  )
  private val authors = List(
    Author(AuthorId("1"), "Martin Odersky"),
    Author(AuthorId("2"), "Bill Venners")
  )

  val TheGreatLibrary = new Library(books, authors)
}
