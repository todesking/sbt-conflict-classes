package com.todesking.sbt_dependency_doctor

import java.io.File

case class JarPath(asFile:java.io.File)
case class JarEntry(name:String)
case class ConflictEntry(entry:JarEntry, jars:Seq[JarPath])

object Plugin extends sbt.AutoPlugin {
  import sbt._

  object autoImport {
    val conflictClasses = taskKey[Unit]("show conflict entries in classpath")
  }
  import autoImport._

  override def trigger = allRequirements

  override val projectSettings =
    forConfig(Compile)

  def forConfig(config:Configuration):Seq[sbt.Def.Setting[_]] = inConfig(config)(seq(
    conflictClasses := {
      val cps = (Keys.dependencyClasspath in config).value
      val conflicts = buildConflicts(cps.map(_.data))

      conflicts.foreach {conflict:ConflictEntry =>
        println(s"Conflict: ${conflict.entry.name}")
        conflict.jars.foreach { jar =>
          println(s"    ${jar.asFile}")
        }
      }
    }
  ))

  def buildConflicts(jars:Seq[File]):Seq[ConflictEntry] = {
    import java.util.{jar => java}
    import scala.collection.JavaConverters._

    val jarFiles = jars.map(jar => new java.JarFile(jar))
    val entryMap:Map[JarEntry, Seq[JarPath]] =
      jarFiles.aggregate(Map.empty[JarEntry, Seq[JarPath]]) ({(map, jar) =>
        val jarPath = JarPath(new File(jar.getName))
        jar.entries.asScala.map(e => JarEntry(e.getName)).
          aggregate(map)({(map, entry) => map + (entry -> (map.getOrElse(entry, Seq()) :+ jarPath)) }, _ ++ _)
      }, _ ++ _ )

    entryMap.
      filter(_ match { case (k, v) =>
        !k.name.endsWith("/") &&
        !k.name.startsWith("META-INF/")
      }).
      filter(_ match { case (k, v) => v.size > 1}).
      map(_ match { case (k, v) => ConflictEntry(k, v) }).
      toSeq.
      sortBy(_.entry.name)
  }
}
