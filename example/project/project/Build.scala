import sbt._
import Keys._

object ApplicationBuild extends Build {
  val project = Project(
    "example",
    file(".")
  ).dependsOn(file("../../"))
}
