name := "AkkaHttpExample"

organization := "com.xyz"
version := "1.0"

scalaVersion := "2.11.8"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.2-RC3")

enablePlugins(JavaAppPackaging)

// Expose following ports in Docker image.
dockerExposedPorts := Seq(9000)


// custom task that copies artifact.
val copyArtifact = taskKey[File]("copies package jar in target")
// First a key is defined and then the process.
copyArtifact := {
  val srcFile = (packageBin in Compile).value
  val destFile = baseDirectory.value / "target" / "copied.jar"
  IO.copyFile(srcFile, destFile)
  destFile
}