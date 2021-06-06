package sql

import cats.effect.{IO, IOApp}
import cats.syntax.flatMap._
import doobie._
import doobie.implicits._
import sql.Doobie04Updates.{dbTransactor, setupPersonTable}

object Doobie05BatchUpdates extends IOApp.Simple {
  def insertMany(ps: List[Person]): ConnectionIO[Int] = {
    val sql = "INSERT INTO person (id, name, age) VALUES (?, ?, ?)"
    Update[Person](sql).updateMany(ps)
  }

  val data = List[Person](
    Person(10, "Maya", Some(24)),
    Person(20, "Ivan", None)
  )

  val insertData = insertMany(data)

  def run: IO[Unit] =
    setupPersonTable.transact(dbTransactor) *>
      insertData.transact(dbTransactor) >>=
      IO.println
}
