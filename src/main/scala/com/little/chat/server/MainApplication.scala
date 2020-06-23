package com.little.chat.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.little.chat.actors.BrokerCumConnectionManager
import com.little.chat.actors.BrokerCumConnectionManager.{GetClients, Poll}
import com.little.chat.response.Response.{ClientRegistered, ClientsResponse, PollSuccess, User, UserMessage}

import scala.concurrent.duration._
import scala.io.StdIn


object MainApplication extends App {
  implicit val system = ActorSystem("chat-server")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val broker = system.actorOf(BrokerCumConnectionManager.props(), "broker-actor")


  val myExceptionHandler = ExceptionHandler {
    case _: Throwable =>
      complete(StatusCodes.NotFound, "Client doesn't exist")

  }

  val route = cors() {
    handleExceptions(myExceptionHandler) {
      implicit val timeout: Timeout = 5.seconds
      path("poll" / JavaUUID) { id =>
        get {
          complete((broker ? Poll(id)).mapTo[PollSuccess])
        }
      } ~ path("login") {
        post {
          entity(as[User]) { registerRequest: User =>
            broker ! registerRequest
            complete(ClientRegistered(s"Client with ${registerRequest.id} successfully registered"))
          }
        }
      } ~ path("send-message") {
        post {
          entity(as[UserMessage]) { userMessage: UserMessage =>
            broker ! userMessage
            complete(StatusCodes.OK)
          }
        }
      } ~ path("clients") {
        get {
          complete {
            StatusCodes.OK -> (broker ? GetClients).mapTo[ClientsResponse]
          }
        }
      }
    }
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
