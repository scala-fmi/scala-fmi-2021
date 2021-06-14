package fmi.codecs

import fmi.library.{Author, AuthorId, Book, BookId, BookSummary}
import io.circe.Codec

object LibraryCodecs {
  import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
  import io.circe.generic.semiauto._

  implicit val authorIdCodec: Codec[AuthorId] = deriveUnwrappedCodec
  implicit val authorCodec: Codec[Author] = deriveCodec

  implicit val bookIdCodec: Codec[BookId] = deriveUnwrappedCodec
  implicit val bookCodec: Codec[Book] = deriveCodec

  implicit val bookSummaryCodec: Codec[BookSummary] = deriveCodec
}
