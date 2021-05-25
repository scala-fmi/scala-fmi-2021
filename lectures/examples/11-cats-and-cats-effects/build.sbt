name := "cats-and-cats-effects"
version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.0",
  "org.typelevel" %% "cats-effect" % "3.1.1",
  "org.typelevel" %% "spire" % "0.17.0",
  "org.scalatest" %% "scalatest" % "3.2.7" % Test,
  "org.typelevel" %% "cats-laws" % "2.6.0" % Test,
  "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test,
)
