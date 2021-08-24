val akkaHttp        = "com.typesafe.akka" %% "akka-http"            % "10.1.12"
val akkaStream      = "com.typesafe.akka" %% "akka-stream"          % "2.5.26"
val akkaMarshalling = "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12"
val typeSafeConfig  = "com.typesafe"      %  "config"               % "1.3.4"
val akkaCors        = "ch.megard"         %% "akka-http-cors"       % "1.0.0"
val h2db            = "com.h2database"    %  "h2"                   % "1.4.200"
val doobieCore      = "org.tpolecat"      %% "doobie-core"          % "0.9.0"
val doobieH2        = "org.tpolecat"      %% "doobie-h2"            % "0.9.0"
val jwt             = "com.jason-goodwin" %% "authentikat-jwt"      % "0.4.5"



lazy val root = (project in file(".")).
  settings(
    name := "simple-chat-server",
    version := "0.1",
    scalaVersion := "2.12.8",
    mainClass in (Compile, run) := Some("com.little.chat.server.MainApplication"),
    libraryDependencies ++= Seq(
      akkaHttp,
      akkaMarshalling,
      akkaStream,
      akkaCors,
      h2db,
      doobieCore,
      doobieH2,
      jwt
    )
  )




