name := "shopping-app"
version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.0",
  "org.typelevel" %% "cats-effect" % "3.1.1",

  "co.fs2" %% "fs2-core" % "3.0.4",
  "co.fs2" %% "fs2-io" % "3.0.4",
  "co.fs2" %% "fs2-reactive-streams" % "3.0.4",

  "org.http4s" %% "http4s-dsl" % "0.23.0-RC1",
  "org.http4s" %% "http4s-blaze-server" % "0.23.0-RC1",
  "org.http4s" %% "http4s-blaze-client" % "0.23.0-RC1",
  "org.http4s" %% "http4s-circe" % "0.23.0-RC1",

  "org.flywaydb" % "flyway-core" % "7.9.1",

  "org.tpolecat" %% "doobie-core" % "1.0.0-M5",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-M5",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-M5",

  "com.typesafe" % "config" % "1.4.1",

  "io.circe" %% "circe-generic" % "0.14.0",
  "io.circe" %% "circe-generic-extras" % "0.14.0",
  "io.circe" %% "circe-config" % "0.8.0",

  "org.mindrot" % "jbcrypt" % "0.3m",

  "org.reactormonk" %% "cryptobits" % "1.3",

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.scalatest" %% "scalatest" % "3.2.7" % Test,
  "org.typelevel" %% "cats-laws" % "2.6.0" % Test,
  "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test,
)

fork := true
