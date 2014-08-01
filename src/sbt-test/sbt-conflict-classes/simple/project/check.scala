import sbt._
import com.todesking.sbt_conflict_classes.{Conflict, Plugin, Resource}

object check {

  private val expectResources = Set(
    Resource("org/apache/commons/collections/ArrayStack.class"),
    Resource("org/apache/commons/collections/FastHashMap$KeySet.class"),
    Resource("org/apache/commons/collections/FastHashMap.class"),
    Resource("org/apache/commons/collections/FastHashMap$EntrySet.class"),
    Resource("org/apache/commons/collections/FastHashMap$1.class"),
    Resource("org/apache/commons/collections/FastHashMap$Values.class"),
    Resource("org/apache/commons/collections/FastHashMap$CollectionView$CollectionViewIterator.class"),
    Resource("org/apache/commons/collections/FastHashMap$CollectionView.class")
  )

  private val expectClasspath = Set(
    "commons-beanutils-1.7.0.jar",
    "commons-collections-3.2.1.jar"
  )

  val setting = TaskKey[Unit]("check") <<= (Plugin.conflictClasses in Compile).map{
    case Seq(conflict) =>
      assert(conflict.resources == expectResources, conflict.resources + " is not equals " + expectResources.toString)
      val classpath = conflict.classpathes.map(_.asFile.getName)
      assert(classpath == expectClasspath, classpath + " is not equals " + expectClasspath.toString)
    case other =>
      assert(false, other)
  }

}
