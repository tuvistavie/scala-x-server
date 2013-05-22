package com.tuvistavie.xserver.protocol

import akka.util.ByteString

import com.tuvistavie.xserver.io.ByteOrder

trait ByteSerializable {
  val byteOrder: ByteOrder
  implicit val endianness = byteOrder.endian
  def toBytes: ByteString
}

trait ByteDeserializable[T] {
  def fromBytes(b: ByteString): T
}
