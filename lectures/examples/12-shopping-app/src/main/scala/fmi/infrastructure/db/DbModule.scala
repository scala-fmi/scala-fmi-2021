package fmi.infrastructure.db

import cats.effect.IO
import cats.effect.kernel.Resource
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import fmi.infrastructure.db.DoobieDatabase.DbTransactor

case class DbModule(dbTransactor: DbTransactor)

object DbModule {
  def apply(dbConfig: DbConfig): Resource[IO, DbModule] = for {
    _ <- Resource.eval(new DbMigrator(dbConfig, "classpath:/db-migrations").migrate())

    connectionEc <- ExecutionContexts.fixedThreadPool[IO](dbConfig.connectionPoolSize)
    transactor <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      dbConfig.jdbcUrl,
      dbConfig.user,
      dbConfig.password,
      connectionEc
    )
  } yield DbModule(transactor)
}
