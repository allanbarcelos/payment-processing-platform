name := "fraud-detection-service"

version := "0.1"

scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  // "org.apache.kafka" %% "kafka-streams-scala" % "3.5.0",
  // "io.circe" %% "circe-core" % "0.15.0",
  // "io.circe" %% "circe-generic" % "0.15.0",
  // "io.circe" %% "circe-parser" % "0.15.0",
  // "ch.qos.logback" % "logback-classic" % "1.4.8"

  "org.apache.kafka" %% "kafka-streams-scala" % "3.5.0",
  "io.circe" %% "circe-core" % "0.14.3",
  "io.circe" %% "circe-generic" % "0.14.3",
  "io.circe" %% "circe-parser" % "0.14.3",
  "ch.qos.logback" % "logback-classic" % "1.4.8"
)

// Configuração do plugin sbt-assembly
enablePlugins(sbtassembly.AssemblyPlugin)

assembly / test := {}

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) =>
    xs.map(_.toLowerCase) match {
      case "manifest.mf" :: Nil => MergeStrategy.discard
      case "index.list" :: Nil => MergeStrategy.discard
      case "dependencies" :: Nil => MergeStrategy.discard
      case _ => MergeStrategy.first
    }
  case _ => MergeStrategy.first
}
