package com.tuvistavie.xserver.protocol.request

import akka.actor.IO
import akka.util.ByteIterator
import com.typesafe.scalalogging.slf4j.Logging
import com.tuvistavie.xserver.backend.util.{ ExtendedByteIterator, Conversions }

import com.tuvistavie.xserver.protocol.Atom
import ExtendedByteIterator._
import Conversions._

trait Request {
  val opCode: Int
}

trait RequestGenerator {
  def parseRequest(iterator: ByteIterator, data: Int)(implicit endian: java.nio.ByteOrder): Request
}

trait HasReply

trait HasLocalReply extends HasReply

trait NeedsTransfer

object Request extends Logging {

  def getRequest(opCode: Int)(implicit endian: java.nio.ByteOrder, socket: IO.SocketHandle): IO.Iteratee[Request] = {
    for {
      header <- IO.take(3)
      iterator = header.iterator
      data = iterator.getByte
      length = (iterator.getShort - 1) * 4  // in bytes without header length
      _ = logger.debug(s"handling request with opcode ${opCode} and length ${length}")
      request <- IO.take(length)
    } yield {
      val iterator = request.iterator
      generators get opCode match {
        case Some(g) => g.parseRequest(iterator, data)
        case None => BadRequest
      }
    }
  }

  val generators: Map[Int, RequestGenerator] = Map(
    1   -> CreateWindowRequest,
    2   -> ChangeWindowAttributesRequest,
    8   -> MapWindowRequest,
    20  -> GetPropertyRequest,
    55  -> CreateGCRequest,
    70  -> PolyFillRectangleRequest,
    74  -> PolyText8Request,
    98  -> QueryExtensionRequest
  )
}

case object BadRequest extends Request {
  val opCode = 1
}

case class QueryExtensionRequest (
  name: String
) extends Request
  with HasLocalReply {
  val opCode = 98
}

object QueryExtensionRequest extends RequestGenerator with Logging {
  override def parseRequest(iterator: ByteIterator, data: Int)(implicit endian: java.nio.ByteOrder) = {
    val n = iterator.getShort.toInt
    iterator.skip(2)
    val name = iterator.getString(n)
    iterator.skip(n padding)
    QueryExtensionRequest(name)
  }
}

case class GetPropertyRequest (
  window: Int,
  property: Atom,
  propType: Atom,
  longOffset: Int,
  longLength: Int,
  delete: Boolean
) extends Request
  with HasLocalReply {
    val opCode = 20
  }

object GetPropertyRequest extends RequestGenerator {
  override def parseRequest(iterator: ByteIterator, data: Int)(implicit endian: java.nio.ByteOrder) = {
    GetPropertyRequest(
      iterator.getInt,
      Atom.fromValue(iterator.getInt),
      Atom.fromValue(iterator.getInt),
      iterator.getInt,
      iterator.getInt,
      data != 0
    )
  }
}
