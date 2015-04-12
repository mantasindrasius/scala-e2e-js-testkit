name := "js-e2e-testkit"

organization := "lt.indrasius"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.5"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

testFrameworks += TestFramework("lt.indrasius.e2e.js.sbt.Framework")

libraryDependencies += "org.specs2" %% "specs2" % "2.4.17" % "test"

libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.twitter" %% "util-core" % "6.23.0"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.11"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"

libraryDependencies += "org.scala-sbt" % "test-interface" % "1.0"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-core" % "0.6.5" % "test",
  "org.http4s" %% "http4s-server" % "0.6.5" % "test",
  "org.http4s" %% "http4s-blazeserver" % "0.6.5" % "test",
  "org.http4s" %% "http4s-dsl" % "0.6.5" % "test",
  "org.http4s" %% "http4s-json4s" % "0.6.5" % "test",
  "org.http4s" %% "http4s-json4s-native" % "0.6.5" % "test"
)
