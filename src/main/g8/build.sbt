name := "$name;format="normalize"$"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.11"

val circeVersion = "0.8.0"

libraryDependencies += "com.tumblr" %% "colossus" % "0.8.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies += "commons-codec" % "commons-codec" % "1.10"