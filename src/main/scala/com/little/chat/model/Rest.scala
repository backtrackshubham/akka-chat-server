package com.little.chat.model

sealed trait Rest

sealed trait Request extends Rest

sealed trait Response extends Rest

trait Secured extends Request with Response {
  val token: String
}

trait Unsecured extends Request with Response
