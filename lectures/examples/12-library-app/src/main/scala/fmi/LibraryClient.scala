package fmi

import cats.effect.kernel.Resource
import cats.effect.{IO, IOApp}
import cats.implicits._
import fmi.client.{LibraryApi, LibraryClientUI}
import fmi.library.{Author, Book}
import org.http4s.blaze.client.BlazeClientBuilder

case class BookWithAuthors(book: Book, authors: List[Author])

object LibraryClient extends IOApp.Simple {
  val app = for {
    computeExecutionContext <- Resource.liftK(IO.executionContext)

    client <- BlazeClientBuilder[IO](computeExecutionContext).resource

    libraryApi = new LibraryApi(client)
    libraryClientUI = new LibraryClientUI(libraryApi)
  } yield libraryClientUI

  def run: IO[Unit] =
    app.use(ui => ui.selectBook)
      .onCancel(IO.println("Thank you for browsing our library :)!"))
}
