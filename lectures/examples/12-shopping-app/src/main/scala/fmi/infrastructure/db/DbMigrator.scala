package fmi.infrastructure.db

import cats.effect.IO
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.{CleanResult, MigrateResult}

class DbMigrator(dbConfig: DbConfig, migrationsLocation: String) {
  private val flyway: Flyway = Flyway
    .configure()
    .dataSource(dbConfig.jdbcUrl, dbConfig.user, dbConfig.password)
    .schemas(dbConfig.schema)
    .locations(migrationsLocation)
    .table("flyway_schema_history")
    .baselineOnMigrate(true)
    .load()

  def migrate(): IO[MigrateResult] = IO.blocking(flyway.migrate())

  def clean(): IO[CleanResult] = IO.blocking(flyway.clean())
}
