package com.little.chat.repository

import com.little.chat.model.request.Request.{RegisterUser, UserCreatedResponse}
import doobie.implicits._
import doobie.util.fragment

import scala.concurrent.{ExecutionContext, Future}

class UserRepository(implicit val ec: ExecutionContext) extends Repository {

  def checkUserWithNumber(phoneNumber: String): Future[Option[String]] = {
    println(s"going to check for $phoneNumber")
    val userQuery: doobie.ConnectionIO[String] = sql"select phone_number from reg_users where phone_number=$phoneNumber".query[String].unique

    userQuery.transact(xa).unsafeToFuture().map(x => Some(x)).recover {
      case x =>
        println(s"${x}")
        None
    }
  }

  def test_ifUserAlredayExist(phoneNumber: String) = {
    sql"select id, firebase_id, email, name, phone_number from users where phone_number=$phoneNumber".query[UserCreatedResponse].unique.transact(xa).unsafeToFuture()
  }

  def registerUser(regRequest: RegisterUser):Future[Option[UserCreatedResponse]] = {
    val userID = java.util.UUID.randomUUID.toString
    val email = com.little.chat.util.helpers.dummyEmail
    val name = com.little.chat.util.helpers.dummyName

    val userQuery: fragment.Fragment = sql"insert into users(id, firebase_id, name, email, phone_number) values ($userID, ${regRequest.firebaseId}, $name,  $email, ${regRequest.phoneNumber})"

    userQuery.update.run.transact(xa).unsafeToFuture().map{ _ =>
      Some(UserCreatedResponse(userID,regRequest.firebaseId,email,name, regRequest.phoneNumber))
    }.recoverWith{
     case x =>
       test_ifUserAlredayExist(regRequest.phoneNumber).map(Some(_))
    }
  }

}
