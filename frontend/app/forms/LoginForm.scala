package com.tuvistavie.xserver.frontend

import play.api.data._
import play.api.data.Forms._


package object forms {
  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("wrong username or password", result => result match {
      case (username, password) => true
    })
  )
}