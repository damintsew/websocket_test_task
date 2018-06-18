name := "damintsev_test_task"

scalaVersion := "2.12.6"

javacOptions ++= Seq( "-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.9",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1"
)

