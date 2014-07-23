package com.todesking.sbt_dependency_doctor

import java.io.File

case class JarPath(asFile:java.io.File)
case class JarEntry(name:String)
case class ConflictEntry(entries:Set[JarEntry], jars:Set[JarPath])

object Plugin extends sbt.Plugin {
  import sbt._

  val conflictClasses = TaskKey[Unit]("conflict-classes", "show conflict classes in classpath")

  override lazy val settings =
    forConfig(Compile) ++ forConfig(Test) ++ forConfig(Runtime)

  def forConfig(config:Configuration) = inConfig(config)(Seq(
    conflictClasses <<= (Keys.dependencyClasspath in config) map { cps =>
      val conflicts = buildConflicts(cps.map(_.data))

      println("Listing conflict classes:")
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
