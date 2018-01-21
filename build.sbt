name := "YieldEstimator"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
//resolvers += "stephenjudkins-bintray" at "http://dl.bintray.com/stephenjudkins/maven"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.5",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "junit" % "junit" % "4.12" % "test"

)