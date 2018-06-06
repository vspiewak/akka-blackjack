name := "akka-blackjack"
version := "1.0"

scalaVersion := "2.12.6"

lazy val logbackVersion = "1.2.3"
lazy val akkaVersion = "2.5.12"
lazy val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)
