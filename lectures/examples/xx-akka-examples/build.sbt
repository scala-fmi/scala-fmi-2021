name := "akka-examples"
version := "0.1"

scalaVersion := "2.13.6"

val akkaVersion = "2.6.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.2.4",

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.scalatest" %% "scalatest" % "3.2.5" % Test
)
