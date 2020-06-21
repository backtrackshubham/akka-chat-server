package com.little.chat.response

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object Response {

  case class User(id: String, name: String)

  case class UserMessage(to: String, from: String, message: String, time: Long)


  object UserMessage {
    implicit val UserMessageFormat: RootJsonFormat[UserMessage] = jsonFormat4(UserMessage.apply)
  }


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


  case class PollSuccess(unread: List[UnreadMessage] = List.empty[UnreadMessage])

  case class PollFailed(message: String = "Your session has been expired") extends Throwable


  case class UnreadMessage(from: String, messages: List[UserMessage])

  case object UnreadMessage {
    implicit val UnreadMessageFormat: RootJsonFormat[UnreadMessage] = jsonFormat2(UnreadMessage.apply)
  }


  object PollFailed {
    implicit val PollFailedFormat: RootJsonFormat[PollFailed] = jsonFormat1(PollFailed.apply)
  }


  object PollSuccess {
    implicit val PollSuccessFormat: RootJsonFormat[PollSuccess] = jsonFormat1(PollSuccess.apply)
  }


}
