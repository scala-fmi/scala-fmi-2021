name := "cats-and-cats-effects"
version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.0",
  "org.typelevel" %% "cats-effect" % "3.1.1",

  "org.tpolecat" %% "doobie-core" % "1.0.0-M5",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-M5",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-M5",

  "com.typesafe" % "config" % "1.4.1",

  "co.fs2" %% "fs2-core" % "3.0.4",
  "co.fs2" %% "fs2-io" % "3.0.4",
  "co.fs2" %% "fs2-reactive-streams" % "3.0.4",

  "org.http4s" %% "http4s-dsl" % "0.23.0-RC1",
  "org.http4s" %% "http4s-blaze-server" % "0.23.0-RC1",
  "org.http4s" %% "http4s-blaze-client" % "0.23.0-RC1",
  "org.http4s" %% "http4s-circe" % "0.23.0-RC1",

  "org.scalatest" %% "scalatest" % "3.2.7" % Test,
  "org.typelevel" %% "cats-laws" % "2.6.0" % Test,
  "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test,
)
