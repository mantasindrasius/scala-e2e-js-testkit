name := "js-e2e-testkit"

organization := "lt.indrasius"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += "org.specs2" %% "specs2" % "2.4.17" % "test"

libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.twitter" %% "util-core" % "6.23.0"

libraryDependencies += "lt.indrasius" %% "embedded-server" % "1.0-SNAPSHOT" % "test"

libraryDependencies += "lt.indrasius" %% "http-testkit" % "1.0-SNAPSHOT" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.11"
