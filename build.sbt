enablePlugins(JavaAppPackaging)

herokuAppName in Compile := "http4s-todo"

herokuProcessTypes in Compile := Map(
  "web" -> "target/universal/stage/bin/http4s-todo -Dhttp.port=$PORT"
)

val Http4sVersion = "0.18.11"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"
val CirceVersion = "0.9.3"
val TypeSafeLogging = "3.9.0"

lazy val root = (project in file("."))
  .settings(
    organization := "iamquang95",
    name := "todo",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % TypeSafeLogging,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    )
  )
