name := "akka-remote-actor-flood-test"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-feature")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= {
  val akkaVersion = "2.3.0"
  Seq(
    "com.typesafe"                  %% "scalalogging-slf4j" % "1.0.1",
    "ch.qos.logback"                %  "logback-classic"    % "1.0.13",
    "com.typesafe.akka"             %% "akka-slf4j"         % akkaVersion,
    "com.typesafe.akka"             %% "akka-actor"         % akkaVersion,
    "com.typesafe.akka"             %% "akka-remote"        % akkaVersion,
    "com.typesafe.akka"             %% "akka-testkit"       % akkaVersion  % "test",
    "org.scalatest"                 %% "scalatest"          % "2.0"        % "test"
  )
}