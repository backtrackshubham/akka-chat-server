package com.little.chat.server
import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._
import com.little.chat.repository.Repository

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
object Main2 extends App with Repository {
  val program1 = sql"select email from users where phone_number = '+919944684536'".query[String].unique

  import scala.concurrent.ExecutionContext.Implicits.global
  println("running the query")
  println(Await.result(program1.transact(xa).unsafeToFuture().map(s => Some(s)).recover{
    case _ => None
  }, Duration.Inf))
  println("ran the query")

}
