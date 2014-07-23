organization := "com.todesking.example"

name := "example"

libraryDependencies ++= Seq(
  "org.apache.hbase" % "hbase" % "0.94.3" % "compile",
  "org.specs2" %% "specs2" % "2.2" % "test"
)
