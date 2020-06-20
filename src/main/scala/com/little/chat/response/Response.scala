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


  trait PollResponse

  case object PollSuccess extends PollResponse
  case class PollFailed(message: String = "Your session has been expired") extends PollResponse

  object PollFailed {
    implicit val PollFailedFormat: RootJsonFormat[PollFailed] = jsonFormat1(PollFailed.apply)
  }

}
