package com.little.chat.util

object helpers {
  val chars = ('a' to 'z').toList.map(_.toString) ::: (0 to 9).toList.map(_.toString)
  val domains = List(
    "@gmail.com",
    "@yahoo.com",
    "@apple.com",
    "@live.com",
    "@hotmail.com"
  )

  def dummyEmail: String = {
    val userName = (1 to 10).map(_ => chars(util.Random.nextInt(chars.length))).mkString("")
    val domain = domains(util.Random.nextInt(domains.length))
    s"$userName$domain"
  }

  def dummyName = (1 to 10).map(_ => chars(util.Random.nextInt(chars.length))).mkString("")
}
