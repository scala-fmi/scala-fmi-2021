package fmi.library

import cats.effect.IO

case class BookId(id: String) extends AnyVal
case class Book(id: BookId, name: String, authors: List[AuthorId], genre: String)

case class AuthorId(id: String) extends AnyVal
case class Author(id: AuthorId, name: String)

class Library(books: List[Book], authors: List[Author]) {
  private val bookIdToBook = books.map(book => book.id -> book).toMap
  private val authorIdToAuthor = authors.map(author => author.id -> author).toMap

  def findBook(bookId: BookId): IO[Option[Book]] = IO.pure(bookIdToBook.get(bookId))

  def findAuthor(authorId: AuthorId): IO[Option[Author]] = IO.pure(authorIdToAuthor.get(authorId))

  def allBooks: IO[List[Book]] = IO.pure(bookIdToBook.values.toList)
}

object Library {
  private val books = List(
    Book(BookId("1"), "Programming in Scala", List(AuthorId("1"), AuthorId("2")), "Computer Science"),
    Book(BookId("2"), "Programming Erlang", List(AuthorId("3")), "Computer Science"),
    Book(BookId("3"), "American Gods", List(AuthorId("4")), "Fantasy"),
    Book(BookId("4"), "The Fellowship of the Ring", List(AuthorId("5")), "Fantasy"),
    Book(BookId("5"), "The Book", List(
      AuthorId("1"), AuthorId("3"), AuthorId("4"), AuthorId("5")
    ), "Fantasy")
  )
  private val authors = List(
    Author(AuthorId("1"), "Martin Odersky"),
    Author(AuthorId("2"), "Bill Venners"),
    Author(AuthorId("3"), "Joe Armstrong"),
    Author(AuthorId("4"), "Neil Gaiman"),
    Author(AuthorId("5"), "J. R. R. Tolkien")
  )

  val TheGreatLibrary = new Library(books, authors)
}
