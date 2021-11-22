// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

name := "surge-samples"

version := "0.1"

ThisBuild / scalaVersion := "2.13.5"

publish / skip := true

fork := true

lazy val app = (project in file("modules/app"))
  .settings(
    libraryDependencies ++= Seq(
      Akka.http,
      akkaHttpPlayJson,
      surge,
      gatling,
      gatlingFramework,
      "org.graalvm.sdk" % "graal-sdk" % "20.0.0",
      "org.graalvm.truffle" % "truffle-api" % "20.0.0",
      "org.graalvm.js" % "js" % "20.0.0",
      "com.lihaoyi" %% "os-lib" % "0.7.1"
    ),
    publish / skip := true
  )
  .enablePlugins(JavaServerAppPackaging)
