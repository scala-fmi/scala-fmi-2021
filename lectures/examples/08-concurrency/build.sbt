name := "concurrency"
version := "0.1"

scalaVersion := "2.13.5"

val akkaVersion = "2.6.13"

libraryDependencies ++= Seq(
  "io.monix" %% "monix" % "3.3.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.2.4",
  "org.asynchttpclient" % "async-http-client" % "2.12.3",
  "org.scalatest" %% "scalatest" % "3.2.5" % Test
)
