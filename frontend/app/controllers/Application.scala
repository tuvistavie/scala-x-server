package com.tuvistavie.xserver.frontend.controllers

import play.api._
import play.api.mvc._

import com.tuvistavie.xserver.bridge.BridgeManager
import com.tuvistavie.xserver.frontend.forms._
import com.tuvistavie.xserver.frontend.auth.{ NixLoginManager, DummyLoginManager }

object Application extends Controller {
  implicit val app = Play.current

  val loginManager = Play.mode match {
    case Mode.Prod => NixLoginManager
    case _ => DummyLoginManager
  }

  val indexRoute = com.tuvistavie.xserver.frontend.controllers.routes.Application.index
  val loginRoute = com.tuvistavie.xserver.frontend.controllers.routes.Application.login
  val tokenName = Play.current.configuration.getString("auth.token-name").get

  def index = Action { implicit request =>
    session.get(tokenName) match {
      case Some(tok) => {
        loginManager.authenticate(tok) match {
          case Some(user) => {
            BridgeManager.create(user)
            Ok(views.html.index("Your new application is ready."))
          }
          case None => Redirect(loginRoute)
        }
      }
      case None => Redirect(loginRoute)
    }
  }

  def login = Action {
    Ok(views.html.login())
  }

  def doLogin() = Action { implicit request =>
    loginForm.bindFromRequest.fold (
      formWithErrors => BadRequest(views.html.login()), {
        case (username, password) => loginManager.authenticate(username, password) match {
          case Some(user) => Redirect(indexRoute).withSession(
              tokenName -> user.token
            )
          case None => BadRequest(views.html.login())
        }
      }
    )
  }
}
