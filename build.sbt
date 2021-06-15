

name := "ScalaExercise"

version := "0.1"

scalaVersion := "2.12.14"

val akkaVersion = "2.5.13"
val akkaHttpVersion = "10.1.3"
val mongoVersion = "2.4.0"
val json4s = "4.0.0"


lazy val root = (project in file("."))
  .settings(
    compile / logLevel := Level.Info,
    logLevel := Level.Info
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "org.mongodb.scala" %% "mongo-scala-driver" % mongoVersion,
  "org.json4s" %% "json4s-core" % json4s,
  "org.json4s" %% "json4s-native" % json4s,
  "org.json4s" %% "json4s-ext" % json4s,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "joda-time" % "joda-time" % "2.10",
  "org.joda" % "joda-money" % "1.0.1",
)