package com.tuvistavie.xserver.backend.io

import java.net.InetSocketAddress

import akka.actor.{ Actor, ActorRef, IO, IOManager, Props }
import akka.actor.{ ActorLogging, ActorSystem, Terminated }
import akka.util.{ ByteString, ByteStringBuilder }
import com.typesafe.scalalogging.slf4j.Logging

import com.tuvistavie.xserver.backend.util.{ RuntimeConfig, Config }

private class Server(displayNumber: Int) extends Actor with ActorLogging {
  import IO._

  val port = Config.getInt("server.base-port") + displayNumber + Config.getInt("server.display.offset")

  override def preStart {
    IOManager(context.system) listen new InetSocketAddress(port)
    log.info("Server started on port {} ", port)
  }

  def receive = {
    case NewClient(server) => {
      val child = context.actorOf(Props(new ClientManager(Server.currentId)))
      var socket = server.accept()(child)
      log.info("new client connected with uuid: {}", socket.uuid)
    }
    case ClientConnectionAdded(socket, client) => {
      Server.addClient(client)
    }
    case ClientConnectionClosed(clientId) => {
      Server.removeClient(clientId)
    }
  }
}

object Server extends Logging {
  val system = ActorSystem("Server")

  val ref = system.actorOf(Props(new Server(RuntimeConfig.displayNumber)), "mainServer")

  private[this] var clientsById = Map[Int, Client]()
  private var _currentId = 1
  private def currentId = _currentId

  private def addClient(client: Client) {
    clientsById += (currentId -> client)
    logger.debug(s"added client with id: ${currentId}")
    _currentId += 1
  }

  private def removeClient(clientId: Int) {
    clientsById -= clientId
    logger.debug(s"removed client with id: ${clientId}")
  }

  def getClient(clientId: Int): Client = clientsById get (clientId) match {
    case None => throw new NoSuchElementException("no client with id " + clientId)
    case Some(c) => c
  }

  def run() {
    val currentUser = System.getProperty("user.name")
    logger.debug(s"starting server as ${currentUser}")
  }
}
