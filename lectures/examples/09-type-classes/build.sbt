name := "type-classes"
version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.2",
  "org.scala-lang" % "scala-reflect" % "2.13.5",
  "org.typelevel" %% "cats-core" % "2.6.0",
  "org.typelevel" %% "spire" % "0.17.0",
  "org.scalatest" %% "scalatest" % "3.2.7" % Test
)
