package com.little.chat.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.little.chat.model.request.Request.{LoginRequest, RegisterUser}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.http.scaladsl.server.Route
import com.little.chat.repository.UserRepository
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol._
import spray.json._
import spray.json.{RootJsonFormat, jsonWriter}
import com.little.chat.model.request.Request.UserCreatedResponse._

import scala.concurrent.ExecutionContext


trait AuthRoutes {

  val userRepo: UserRepository
  implicit val executionContext: ExecutionContext

  lazy val smyRoutes: Route = pathPrefix("smy") {
    securedRoutes ~ unSecured
  }

  val securedRoutes: Route = pathPrefix("withAuth") {
    complete(StatusCodes.OK)
  }

  val unSecured: Route = path("register") {
    post {
      entity(as[RegisterUser]) { req =>
        complete {
          userRepo
            .registerUser(req).map {
            case Some(userCreated) =>
              HttpResponse(StatusCodes.Created,
                entity = HttpEntity(ContentTypes.`application/json`, userCreated.toJson.toString))
            case None =>
              HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, s"""{"error" : "User not created"}"""),
                status = StatusCodes.Forbidden)
          }
        }
      }
    }
  } ~
    path("login") {
    post {
      entity(as[LoginRequest]) { req =>
        complete(StatusCodes.OK)
      }
    }
  } ~ pathPrefix("checkUser" / Segment) { number: String =>
    get {
      complete{
        userRepo.checkUserWithNumber(number) map {
          case Some(value) =>
            HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, s"""{"response" : "User found with if ${value}"}"""))
          case None =>
            HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, s"""{"error" : "User not found"}"""),
              status = StatusCodes.NotFound)
        }
      }
    }
  }

}
