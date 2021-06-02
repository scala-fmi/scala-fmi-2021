val Http4sVersion = "0.23.0-RC1"
val CirceVersion = "0.13.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    organization := "scala-fmi",
    name := "http4s-and-circe",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,

      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,

      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,

      "org.scalatest" %% "scalatest" % "3.2.7" % Test,
      "org.typelevel" %% "cats-effect-testing-scalatest" % "1.1.1" % Test


//      "org.scalameta"   %% "svm-subs"            % "20.2.0"
    )
  )
