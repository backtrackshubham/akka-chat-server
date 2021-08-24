package com.little.chat.util.sec

import java.util.concurrent.TimeUnit
import akka.actor.ActorRef

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.little.chat.response.Response.User


trait JWTAuthentication {

  val JWT_SECRET_ALGORITHM = "HS256"
  val JWT_SECRET_KEY = "#$%^a&* 9^#"
  val PHONE_NUMBER = "phoneNumber"
  val UNIQUE_ID = "uniqueId"
  val EXPIRE = "expire"
  val EMPTY_STRING = ""

  def getUserInfoByToken(token: String): Option[User] = {
    (for {
      claims <- getClaims(token)
      phoneNumberClaims <- claims.get(PHONE_NUMBER)
      uniqueIdClaims <- claims.get(UNIQUE_ID)
    } yield (phoneNumberClaims, uniqueIdClaims)) map(creds => User(creds._1, creds._2))
  }

  private def getClaims(token: String): Option[Map[String, String]] = token match {
    case JsonWebToken(_, claims, _) => claims.asSimpleMap.toOption
    case _ => None
  }

  def validateAndUpdateToken(token: String): String =
    if (JsonWebToken.validate(token, JWT_SECRET_KEY)) {
      getClaims(token) match {
        case Some(claims) =>
          claims.get(EXPIRE) match {
            case Some(tokenTimeout) => updateToken(token, tokenTimeout)
            case None => ""
          }
        case None => ""
      }
    } else {
      ""
    }

  private def updateToken(token: String, tokenTimeout: String): String = {
    Try(tokenTimeout.toLong) match {
      case Success(tokenTimeoutValue) =>
        if (tokenTimeoutValue - System.currentTimeMillis() > 0) {
          val phoneNumber = getClaims(token).flatMap(claim => claim.get(PHONE_NUMBER)).fold(EMPTY_STRING)(identity)
          val uniqueId = getClaims(token).flatMap(claim => claim.get(PHONE_NUMBER)).fold(EMPTY_STRING)(identity)
          createToken(phoneNumber,uniqueId,1)
        } else ""
      case Failure(exception) =>  ""
    }
  }

  def createToken(phoneNumber: String, uniqueId: String, expireDay: Int): String = {
    val header = JwtHeader(JWT_SECRET_ALGORITHM)
    val claimsSet = setClaims(phoneNumber, uniqueId, expireDay)
    JsonWebToken(header, claimsSet, JWT_SECRET_KEY)
  }

  private def setClaims(phoneNumber: String,uniqueId: String,expiryPeriodInDays: Int) = JwtClaimsSet(
    Map(PHONE_NUMBER -> phoneNumber,
      UNIQUE_ID -> uniqueId,
      EXPIRE -> (System.currentTimeMillis() + TimeUnit.HOURS.toMillis(expiryPeriodInDays))))
}

object JWTAuthentication extends JWTAuthentication
