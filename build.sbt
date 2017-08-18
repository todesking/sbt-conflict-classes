version := "0.0.3-SNAPSHOT"

organization := "com.todesking"

name := "sbt-conflict-classes"

sbtPlugin := true

scalacOptions ++= Seq("-deprecation", "-unchecked")

publishTo := Some(Resolver.file("com.todesking",file("./repo/"))(Patterns(true, Resolver.mavenStyleBasePattern)))

crossSbtVersions := Seq("0.13.16", "1.0.0")
