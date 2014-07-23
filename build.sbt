version := "0.0.2"

organization := "com.todesking"

name := "sbt-conflict-classes"

sbtPlugin := true

scalacOptions ++= Seq("-deprecation", "-unchecked")

CrossBuilding.crossSbtVersions := Seq("0.12", "0.13")

crossBuildingSettings

publishTo := Some(Resolver.file("com.todesking",file("./repo/"))(Patterns(true, Resolver.mavenStyleBasePattern)))

