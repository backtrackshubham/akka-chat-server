val akkaActor = "com.typesafe.akka" %% "akka-http"   % "10.1.12"
val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.5.26"
val akkaMarshalling = "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12"
val akkaCors = "ch.megard" %% "akka-http-cors" % "1.0.0"


lazy val root = (project in file(".")).
  settings(
    name := "simple-chat-server",
    version := "0.1",
    scalaVersion := "2.12.8",
    mainClass in (Compile, run) := Some("com.little.chat.server.MainApplication"),
    libraryDependencies ++= Seq(
      akkaActor,
      akkaMarshalling,
      akkaStream,
      akkaCors
    )
  )




