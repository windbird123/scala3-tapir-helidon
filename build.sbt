val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-migration",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-nima-server" % "1.10.3",
      "com.softwaremill.sttp.tapir" %% "tapir-files" % "1.10.4",
      "io.helidon.webserver" % "helidon-webserver" % "4.0.7",
      "io.helidon.config" % "helidon-config-hocon" % "4.0.7",
      "io.helidon.http.encoding" % "helidon-http-encoding-gzip" % "4.0.7",
      "io.helidon.http.encoding" % "helidon-http-encoding-deflate" % "4.0.7",
      "org.typelevel" %% "cats-core" % "2.10.0",
      "org.wvlet.airframe" %% "airframe" % "24.3.0",
      "com.softwaremill.ox" %% "core" % "0.0.25",
      "org.playframework" %% "play-json" % "3.0.2"
    )
  )
