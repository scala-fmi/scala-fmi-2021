package fmi.library

object BookSummary {
  def apply(book: Book): BookSummary = BookSummary(book.id, book.name)
}

case class BookSummary(id: BookId, name: String)