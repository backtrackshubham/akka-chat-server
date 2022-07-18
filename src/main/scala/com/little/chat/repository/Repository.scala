// package com.little.chat.repository
// import doobie._
// import doobie.implicits._
// import cats._
// import cats.effect._
// import cats.implicits._

// import scala.concurrent.Future

// trait Repository {
//   // We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
//   // is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
//   implicit val cs = IO.contextShift(scala.concurrent.ExecutionContext.Implicits.global)

//   // A transactor that gets connections from java.sql.DriverManager and executes blocking operations
//   // on an our synchronous EC. See the chapter on connection handling for more info.
//   val xa = Transactor.fromDriverManager[IO](
//     "org.h2.Driver",     // driver classname
//     "jdbc:h2:mem:test;MODE=MySQL;DATABASE_TO_UPPER=false;INIT=RUNSCRIPT FROM './src/main/resources/schema.sql'\\;RUNSCRIPT FROM './src/main/resources/initdata.sql'",     // connect URL (driver-specific)
//     "root",                  // user
//     "",                          // password
//     Blocker.liftExecutionContext(ExecutionContexts.fixedThreadPool(10)) // just for testing
//   )
// }
