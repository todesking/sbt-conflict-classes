package com.todesking.sbt_dependency_doctor

import java.io.File

case class JarPath(asFile:java.io.File)
case class JarEntry(name:String)
case class ConflictEntry(entries:Set[JarEntry], jars:Set[JarPath])

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
        println(s"Found conflict classes in jars:")
        conflict.jars.toSeq.sortBy(_.asFile.name).foreach { jar =>
          println(s"    ${jar.asFile}")
        }
        println(s"  with classes:")
        conflict.entries.toSeq.sortBy(_.name).foreach { entry =>
          println(s"    ${entry.name}")
        }
      }
    }
  ))

  def buildConflicts(jars:Seq[File]):Seq[ConflictEntry] = {
    import java.util.{jar => java}
    import scala.collection.JavaConverters._

    val jarFiles = jars.map(jar => new java.JarFile(jar))

    val entryToJars:Map[JarEntry, Seq[JarPath]] =
      jarFiles.foldLeft(Map[JarEntry, Seq[JarPath]]()) {(map, jar) =>
        val jarPath = JarPath(new File(jar.getName))
        jar.entries.asScala.map(e => JarEntry(e.getName))
          .filter { entry =>
            !entry.name.endsWith("/") &&
            !entry.name.startsWith("META-INF/")
          }
          .foldLeft(map) {(map, entry) => map + (entry -> (map.getOrElse(entry, Seq()) :+ jarPath)) }
      }

    val jarsToEntries:Map[Set[JarPath], Set[JarEntry]] = entryToJars.foldLeft(Map[Set[JarPath], Set[JarEntry]]()) { (map, kv) =>
      kv match { case (entry, jars) =>
        val jarSet = jars.toSet
        map + (jarSet -> (map.getOrElse(jarSet, Set()) + entry))
      }
    }

    jarsToEntries.filter { case (jars, _) => jars.size > 1 }.map {case (jars, entries) => ConflictEntry(entries, jars) }.toSeq
  }
}
