package com.tuvistavie.xserver.backend.util

import akka.util.{ ByteStringBuilder, ByteIterator }


class ExtendedByteStringBuilder(builder: ByteStringBuilder) {
  import Conversions._

  def fill(n: Int) {
    (1 to n) foreach { _ => builder.putByte(0) }
  }

  def writePadding(n: Int) {
    fill(n.padding)
  }

  def putBoolean(b: Boolean) {
    if(b) builder.putByte(0)
    else builder.putByte(1)
  }
}

object ExtendedByteStringBuilder {
  implicit def byteStringBuilderToExtendedByteStringBuilder(builder: ByteStringBuilder) = new ExtendedByteStringBuilder(builder)
}

class ExtendedByteIterator(iterator: ByteIterator) {
  import Conversions._

  def skip(n: Int) {
    (1 to n) foreach { _ => iterator getByte }
  }

  def skipPadding(n: Int) {
    skip(n padding)
  }

  def getString(n: Int): String = {
    val byteArray: Array[Byte] = new Array[Byte](n)
    iterator.getBytes(byteArray)
    byteArray toString
  }
}

object ExtendedByteIterator {
  implicit def byteIteratorToExtendedByteIterator(iterator: ByteIterator) = new ExtendedByteIterator(iterator)
}