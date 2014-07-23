version := "0.0.1"

scalaVersion := "2.10.4"

organization := "com.todesking"

name := "sbt-conflict-classes"

sbtPlugin := true

publishTo := Some(Resolver.file("com.todesking",file("./repo/"))(Patterns(true, Resolver.mavenStyleBasePattern)))
