name := "akka-blackjack"
version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.12"
lazy val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)
