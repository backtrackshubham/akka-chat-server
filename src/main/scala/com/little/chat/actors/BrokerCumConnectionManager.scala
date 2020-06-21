package com.little.chat.actors

import java.util.UUID

import akka.actor.{Actor, Props}
import akka.event.Logging
import com.little.chat.response.Response.{ClientsResponse, PollFailed, PollSuccess, User}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._


object BrokerCumConnectionManager {
  def props()(implicit ec: ExecutionContext): Props = Props(new BrokerCumConnectionManager)

  case object GetClients
  case object RemoveOldClients

  case class Poll(id: UUID)

  private val refreshUsers: FiniteDuration = 10.seconds
  private val initialDelay = 0.seconds
}


class BrokerCumConnectionManager(implicit val ex: ExecutionContext) extends Actor {

  import BrokerCumConnectionManager._
  private val refreshStatusesInterval = context.system.scheduler.schedule(
    initialDelay = initialDelay,
    interval = refreshUsers,
    receiver = self,
    message = RemoveOldClients
  )

  override def postStop(): Unit = {
    super.postStop()
    refreshStatusesInterval.cancel()
    ()
  }
  val log = Logging(context.system, this)

  var clients: Map[User, Long] = Map.empty[User, Long]
  override def receive: Receive = {
    case RemoveOldClients =>
      val cur = System.currentTimeMillis
      clients.filter(res => cur - res._2 > 7000).keys.foreach{ key =>
        log.info(s"Removing user due to inactivity $key")
        clients = clients - key
      }
    case user: User =>
      log.info(s"Adding user $user")
      clients = clients + (user -> System.currentTimeMillis)
    case GetClients =>
            sender() ! ClientsResponse(clients.keys.toList)
    case Poll(id) =>
      log.info(s"Received Poll from $id")
      if(!clients.keys.exists(_.id == id.toString)){
        sender() ! PollFailed()
      } else {
        val userToUpdate = clients.keys.filter(_.id == id.toString)
        clients = clients + (userToUpdate.head -> System.currentTimeMillis)
        sender() ! PollSuccess()
      }
  }
}
