organization := "com.todesking.example"

name := "example"

libraryDependencies ++= Seq(
  "commons-beanutils" % "commons-beanutils" % "1.7.0",
  "commons-collections" % "commons-collections" % "3.2.1"
)

conflictClassExcludes ++= Seq(
  "org/apache/commons/collections/Buf"
)
