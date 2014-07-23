# sbt-conflict-classes: List conflict classes in classpath

## Usage

```sbt
// project/plugins.sbt

resolvers += "com.todesking" at "http://todesking.github.io/mvn/"

addSbtPlugin("com.todesking" %% "sbt-conflict-classes" % "0.0.1")
```

```
$ sbt compile:conflict-classes # show compile-time conflicts
$ sbt test:conflict-classes    # show test-time conflicts
$ sbt runtime:conflict-classes # show runtime conflicts
```
