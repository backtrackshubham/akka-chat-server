package com.little.chat.response

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object Response {

  case class User(id: String, name: String)


  object User {
    implicit val UserFormat: RootJsonFormat[User] = jsonFormat2(User.apply)
  }

  case class ClientsResponse(clients: List[User])

  object ClientsResponse {
    implicit val ClientResponseFormat: RootJsonFormat[ClientsResponse] = jsonFormat1(ClientsResponse.apply)
  }

  case class ClientRegistered(message: String)

  object ClientRegistered {
    implicit val ClientRegisteredFormat: RootJsonFormat[ClientRegistered] = jsonFormat1(ClientRegistered.apply)
  }


  case class PollSuccess(message: String = "Poll successful")
  case class PollFailed(message: String = "Your session has been expired") extends Throwable

  object PollFailed {
    implicit val PollFailedFormat: RootJsonFormat[PollFailed] = jsonFormat1(PollFailed.apply)
  }


  object PollSuccess {
    implicit val PollSuccessFormat: RootJsonFormat[PollSuccess] = jsonFormat1(PollSuccess.apply)
  }


}
