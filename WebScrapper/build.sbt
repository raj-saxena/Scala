name := "WebScrapper"

description := "A simple scrapper to extract the 'title' and 'description' from AWS SDK release notes."

version := "1.0"

scalaVersion := "2.11.8"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.jsoup" % "jsoup" % "1.9.2")

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

