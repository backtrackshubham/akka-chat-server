package com.little.chat.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.little.chat.actors.BrokerCumConnectionManager
import com.little.chat.actors.BrokerCumConnectionManager.{GetClients, Poll}
import com.little.chat.response.Response.{ClientRegistered, ClientsResponse, PollResponse, User}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.little.chat.response.Response

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn


object MainApplication extends App {
  implicit val system = ActorSystem("chat-server")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val broker = system.actorOf(BrokerCumConnectionManager.props(), "broker-actor")

  val route = cors() {
    implicit val timeout: Timeout = 5.seconds
    pathPrefix("chat-with") {
      path(JavaUUID) { _ =>
        post {
          complete(HttpEntity(ContentTypes.`application/json`, "<h1>Say hello to akka-http</h1>"))
        }
      }
    } ~ path("poll" / JavaUUID) { id =>
      get {
        val res: Future[() => StatusCode] = (broker ? Poll(id)).mapTo[PollResponse].map{
          case Response.PollFailed(_) =>
            () => StatusCodes.NotFound
          case Response.PollSuccess =>
            () => StatusCodes.OK
        }
        complete(res.map(f => f()))
      }
    } ~ path("login") {
      post {
        entity(as[User]) { registerRequest : User =>
          broker ! registerRequest
          complete(ClientRegistered(s"Client with ${registerRequest.id} successfully registered"))
        }
      }
    } ~ path("clients") {
      get {
        val res: Future[ClientsResponse] = (broker ? GetClients).mapTo[ClientsResponse]
        complete(res)
      }
    }
  }

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
