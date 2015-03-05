name := "embedded-karma"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += "org.specs2" %% "specs2" % "2.4.17" % "test"

libraryDependencies += "com.twitter" %% "util-core" % "6.23.0"
