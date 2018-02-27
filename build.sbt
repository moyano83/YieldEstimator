name := "YieldEstimator"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
//resolvers += "stephenjudkins-bintray" at "http://dl.bintray.com/stephenjudkins/maven"

val json4sVersion = "3.3.0"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.5",
  "org.scaldi" %% "scaldi" % "0.5.8",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.slf4j" % "slf4j-log4j12" % "1.7.25",
  "org.json4s" %% "json4s-jackson" % json4sVersion,
  "org.json4s" %% "json4s-native" % json4sVersion,
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
  "junit" % "junit" % "4.12" % Test,
  "org.scalamock" %% "scalamock" % "4.0.0" % Test
)

