import sbt._

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.5" % Test
  val mockito = "org.mockito" % "mockito-core" % "1.10.19" % Test

  val guice = "com.google.inject" % "guice" % "3.0"
  val joda = "joda-time" % "joda-time" % "2.9.3"

  val hocon = "com.typesafe" % "config" % "1.3.0"
  val commonsIO = "commons-io" % "commons-io" % "2.5"

  val suuchiVersion = "0.2.11"
  val suuchi = "in.ashwanthkumar" % "suuchi-core" % suuchiVersion

  val grpcVersion = "1.0.1"
  val grpcNetty = "io.grpc" % "grpc-netty" % grpcVersion
  val grpcStub = "io.grpc" % "grpc-stub" % grpcVersion
  val grpcCore = "io.grpc" % "grpc-core" % grpcVersion

  val protobuf3 = "com.google.protobuf" % "protobuf-java" % "3.0.0"

  val guava = "com.google.guava" % "guava" % "19.0"

  val sbProtoRuntime = "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.42" % "protobuf"
  val sbGrpcRuntime = "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.5.42"
  val scaldingArgs = "com.twitter" % "scalding-args_2.10" % "0.16.0"

  val logbackVersion = "1.1.7"
  val logbackCore = "ch.qos.logback" % "logback-core" % logbackVersion
  val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion
  val logging = Seq(logbackCore, logbackClassic)

  val metrics = "io.dropwizard.metrics" % "metrics-core" % "3.1.0"

  val commonDeps = Seq(joda)

  val testDeps = Seq(scalaTest, mockito)

  val serviceDependencies = commonDeps ++ Seq(
    suuchi, grpcNetty, scaldingArgs, hocon, metrics, hocon) ++
    logging ++ testDeps

  def excludeLoggers(module: ModuleID): ModuleID = module.exclude("ch.qos.logback", "logback-classic")
    .exclude("ch.qos.logback", "logback-core")
    .exclude("org.slf4j", "jcl-over-slf4j")
    .exclude("org.slf4j", "log4j-over-slf4j")
    .exclude("org.slf4j", "slf4j-simple")
}
