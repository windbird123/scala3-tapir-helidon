val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-tapir-helidon",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := scala3Version
  )

val tapirVersion = "1.10.4"
val helidonVersion = "4.0.7"

libraryDependencies ++= Seq(
  // tapir swagger docs
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,

  // helidon nima server for tapir
  "com.softwaremill.sttp.tapir" %% "tapir-nima-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-files" % tapirVersion,
  "io.helidon.webserver" % "helidon-webserver" % helidonVersion,
  "io.helidon.config" % "helidon-config-hocon" % helidonVersion,

  // compression
  "io.helidon.http.encoding" % "helidon-http-encoding-gzip" % helidonVersion,
  "io.helidon.http.encoding" % "helidon-http-encoding-deflate" % helidonVersion,

  // logging
  "io.helidon.logging" % "helidon-logging-slf4j" % helidonVersion,
  "org.slf4j" % "slf4j-api" % "2.0.12",
  "org.slf4j" % "jul-to-slf4j" % "2.0.12",
  "ch.qos.logback" % "logback-classic" % "1.5.3",

  // util libs
  "org.typelevel" %% "cats-core" % "2.10.0",
  "org.wvlet.airframe" %% "airframe" % "24.3.0",
  "com.softwaremill.ox" %% "core" % "0.0.25",
  "org.playframework" %% "play-json" % "3.0.2"
)
