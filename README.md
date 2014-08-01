# sbt-conflict-classes: List conflict classes in classpath  [![Build Status](https://secure.travis-ci.org/todesking/sbt-conflict-classes.png?branch=master)](http://travis-ci.org/todesking/sbt-conflict-classes)


## Usage

```scala
// project/plugins.sbt

resolvers += "com.todesking" at "http://todesking.github.io/mvn/"

addSbtPlugin("com.todesking" %% "sbt-conflict-classes" % "0.0.2")
```

```scala
// build.sbt

// Exclude from conflict detection(match with startsWith)(Optional)
conflictClassExcludes ++= Seq(
  "com/todesking/example/DuplicateClass.class",
  "com/todesking/example/dups/"
)
```

```
$ sbt compile:conflict-classes # show compile-time conflicts
$ sbt test:conflict-classes    # show test-time conflicts
$ sbt runtime:conflict-classes # show runtime conflicts
```

## Example output

```
$ cd example
$ sbt conflict-classes
...
[info] Listing conflict classes:
[info] Found conflict classes in:
[info]     xxx/.ivy2/cache/commons-beanutils/commons-beanutils/jars/commons-beanutils-1.7.0.jar
[info]     xxx/.ivy2/cache/commons-collections/commons-collections/jars/commons-collections-3.2.1.jar
[info]   with classes:
[info]     org/apache/commons/collections/ArrayStack.class
[info]     org/apache/commons/collections/Buffer.class
[info]     org/apache/commons/collections/BufferUnderflowException.class
[info]     org/apache/commons/collections/FastHashMap$1.class
[info]     org/apache/commons/collections/FastHashMap$CollectionView$CollectionViewIterator.class
[info]     org/apache/commons/collections/FastHashMap$CollectionView.class
[info]     org/apache/commons/collections/FastHashMap$EntrySet.class
[info]     org/apache/commons/collections/FastHashMap$KeySet.class
[info]     org/apache/commons/collections/FastHashMap$Values.class
[info]     org/apache/commons/collections/FastHashMap.class
```
