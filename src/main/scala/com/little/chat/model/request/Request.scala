package com.little.chat.model.request

import com.little.chat.model.Unsecured
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object Request {
  case class LoginRequest(mobile: String, uniqueId: String) extends Unsecured


  case object LoginRequest {
    implicit val LoginRequestFormat: RootJsonFormat[LoginRequest] = jsonFormat2(LoginRequest.apply)
  }

  case class RegisterUser(
                           firebaseId: String,
                           phoneNumber: String
                         ) extends Unsecured

  case object RegisterUser {
    implicit val registerUserFormat: RootJsonFormat[RegisterUser] = jsonFormat2(RegisterUser.apply)
  }

  case class UserCreatedResponse(
                                  userId: String,
                                  firebaseId: String,
                                  email: String,
                                  name: String,
                                  phoneNumber: String
                                )

  case object UserCreatedResponse {
    implicit val UserCreatedResponseFormat: RootJsonFormat[UserCreatedResponse] = jsonFormat5(UserCreatedResponse.apply)

  }

}
