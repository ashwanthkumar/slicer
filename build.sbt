import java.net.InetAddress

import Dependencies._
import sbt.Keys._
import sbt.Package.ManifestAttributes
import sbt.SbtExclusionRule

val scalaV = "2.11.8"
val appVersion = sys.env.getOrElse("GO_PIPELINE_LABEL", "1.0.0-SNAPSHOT")

ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

lazy val slicerService = (project in file("slicer-service")).
  settings(
    name := "slicer-service",
    libraryDependencies ++= serviceDependencies
  )
  .settings(withAssemblySettings: _*)

lazy val withAssemblySettings = projectSettings ++ slicerAssembly

lazy val projectSettings = net.virtualvoid.sbt.graph.Plugin.graphSettings ++ Seq(
  version := appVersion,
  organization := "in.ashwanthkumar",
  scalaVersion := scalaV,
  resolvers += Resolver.mavenLocal,
  excludeDependencies ++= Seq(
    SbtExclusionRule("cglib", "cglib-nodep"),
    SbtExclusionRule("commons-beanutils", "commons-beanutils"),
    SbtExclusionRule("commons-beanutils", "commons-beanutils-core")
  ),
  parallelExecution in ThisBuild := false,
  scalacOptions ++= Seq("-unchecked", "-feature"
    // , "-Ylog-classpath" // useful while debugging dependency classpath issues
  )
)

lazy val publishSettings = Seq(
  publishArtifact := true,
  packageOptions := Seq(ManifestAttributes(
    ("Built-By", InetAddress.getLocalHost.getHostName)
  )),
  crossScalaVersions := Seq(scalaV, "2.10.6", "2.12.1"),
  publishMavenStyle := true,
  // disable publishing test jars
  publishArtifact in Test := false,
  // disable publishing the main docs jar
  publishArtifact in(Compile, packageDoc) := false,
  // disable publishing the main sources jar
  publishArtifact in(Compile, packageSrc) := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    }
)

lazy val slicerAssembly = Seq(
  assemblyMergeStrategy in assembly := {
    case PathList(ps@_*) if List("package-info.class", "plugin.properties", "mime.types").exists(ps.last.endsWith) => MergeStrategy.first
    case "reference.conf" | "rootdoc.txt" =>
      MergeStrategy.concat
    case "LICENSE" | "LICENSE.txt" =>
      MergeStrategy.discard
    case PathList("META-INF", xs@_*) =>
      xs map {
        _.toLowerCase
      } match {
        case ("manifest.mf" :: Nil) =>
          MergeStrategy.discard
        case ps@(x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") || ps.last.endsWith(".rsa") =>
          MergeStrategy.discard
        case ("log4j.properties" :: Nil) =>
          MergeStrategy.discard
        case _ => MergeStrategy.first
      }
    case _ => MergeStrategy.deduplicate
  }
)
