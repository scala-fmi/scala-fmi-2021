name := "library-app"
version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.0",
  "org.typelevel" %% "cats-effect" % "3.1.1",

  "org.http4s" %% "http4s-dsl" % "0.23.0-RC1",
  "org.http4s" %% "http4s-blaze-server" % "0.23.0-RC1",
  "org.http4s" %% "http4s-blaze-client" % "0.23.0-RC1",
  "org.http4s" %% "http4s-circe" % "0.23.0-RC1",

  "io.circe" %% "circe-generic" % "0.14.0",
  "io.circe" %% "circe-generic-extras" % "0.14.0",

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.scalatest" %% "scalatest" % "3.2.7" % Test
)
