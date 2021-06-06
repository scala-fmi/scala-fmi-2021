package sql

import cats.effect.{IO, IOApp}
import cats.syntax.apply._
import cats.syntax.flatMap._
import cats.syntax.functor._
import doobie._
import doobie.implicits._

case class Person(id: Int, name: String, age: Option[Int])

object Doobie04Updates extends IOApp.Simple {
  val dbTransactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:world",
    "postgres",
    ""
  )

  def setupPersonTable: ConnectionIO[Unit] = {
    val dropPersonTable =
      sql"""
        DROP TABLE IF EXISTS person
      """.update.run
    val createPersonTable =
      sql"""
        CREATE TABLE person (
          id   SERIAL,
          name VARCHAR NOT NULL UNIQUE,
          age  SMALLINT
        )
      """.update.run

    (dropPersonTable, createPersonTable).tupled.as(())
  }

  def insertPerson(name: String, age: Option[Int]): Update0 = {
    sql"""INSERT INTO person(name, age) VALUES ($name, $age)""".update
  }

  def insertAndRetrieve(name: String, age: Option[Int]): ConnectionIO[Person] = for {
    id <- insertPerson(name, age).withUniqueGeneratedKeys[Int]("id")
    person <- sql"SELECT id, name, age FROM person WHERE id = $id".query[Person].unique
  } yield person

  def insertAndRetrieveForPostgres(name: String, age: Option[Int]): ConnectionIO[Person] =
    insertPerson(name, age).withUniqueGeneratedKeys[Person]("id", "name", "age")

  val insertBoyan = insertPerson("Boyan", None).run
  val insertAndRetrieveViktor = insertAndRetrieveForPostgres("Viktor", Some(25))

  def run: IO[Unit] =
    setupPersonTable.transact(dbTransactor) *>
      insertBoyan.transact(dbTransactor) *>
      insertAndRetrieveViktor.transact(dbTransactor) >>=
      IO.println
}
