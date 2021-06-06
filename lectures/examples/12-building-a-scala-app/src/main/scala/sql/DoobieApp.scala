package sql

import cats.effect.{IO, IOApp}
import cats.syntax.flatMap._
import doobie._
import doobie.implicits._

object DoobieApp extends IOApp.Simple {
  val dbTransactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:world",
    "postgres",
    ""
  )

  def run: IO[Unit] = Doobie03Fragments.ex2.transact(dbTransactor) >>= IO.println
}
