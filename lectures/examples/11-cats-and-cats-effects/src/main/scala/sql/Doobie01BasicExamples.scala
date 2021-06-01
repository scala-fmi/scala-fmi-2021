package sql

import cats.syntax.applicative._
import cats.syntax.apply._
import doobie._
import doobie.implicits._

object Doobie01BasicExamples {
  val ex1 = 42.pure[ConnectionIO].map(_ + 1)
  val ex2 = sql"SELECT 42".query[Int].unique

  val randomSelect = sql"SELECT random()".query[Double].unique
  val ex3 = (ex2, randomSelect).tupled
  val ex4 = randomSelect.replicateA(10)
}
