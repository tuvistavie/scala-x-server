package com.tuvistavie.xserver.io

import java.io.InputStream
import java.io.DataInputStream

import com.tuvistavie.xserver.protocol.types._

import com.tuvistavie.util._


abstract class BinaryInputStream(val inputStream: InputStream) extends DataInputStream(inputStream) {

  def skip(n: IntTimes) = {
    n times { readInt8() }
  }

  implicit def readStr(): Str = {
    val n = this.readByte()
    readString8(n)
  }

  def readListOfStr(n: Int) = {
    def loopRead(n: Int, list: List[Str]): List[Str] = n match {
      case 0 => list reverse
      case _ => loopRead(n - 1, readStr() :: list)
    }
    loopRead(n, List())
  }

  def readListOfCard8(n: Int) = {
    def loopRead(n: Int, list: List[Card8]): List[Card8] = n match {
      case 0 => list reverse
      case _ => loopRead(n - 1, readCard8() :: list)
    }
    loopRead(n, List())
  }

  def readList[T](n: Int)(implicit readVal: () => T) = {
    def loopRead(n: Int, list: List[T]): List[T] = n match {
      case 0 => list reverse
      case _ => loopRead(n - 1, readVal() :: list)
    }
    loopRead(n, List())
  }


  def readString8(n: Int) = {
    var bytes = new Array[Byte](n)
    read(bytes, 0, n)
    Str(bytes)
  }

  def readString16(n: Int) = {
    def loopRead(n: Int, str: String): String = n match {
      case 0 => str
      case _ => loopRead(n - 1, str + this.readUnsignedShort())
    }
    Str(loopRead(n, ""))
  }

  def readPad(n: IntValue) = skip(n.padding)

  def readInt8() = Int8(this.readByte())
  def readUInt8() = UInt8(this.readByte())
  def readInt8(n: Int): Int8
  def readUInt8(n: Int): UInt8

  def readInt16(): Int16
  def readUInt16(): UInt16
  def readInt16(n: Int): Int16
  def readUInt16(n: Int): UInt16

  def readUInt32(): UInt32
  def readInt32(): Int32

  def readBool() = Bool(this.readBoolean())
  def readBool(n: Int): Bool

  def readBitGravity() = readCard8().asBitGravity
  def readBitGravity(n: Int) = readCard8(n).asBitGravity
  def readWindowGravity() = readCard8().asWindowGravity
  def readWindowGravity(n: Int) = readCard8(n).asWindowGravity

  def readSetOfKeyMask() = readCard16().asSetOfKeyMask
  def readSetOfKeyButMask() = readCard16().asSetOfKeyButMask

  def readAtom() = readCard32().asAtom
  def readSetOfEvent() = readCard32().asSetOfEvent
  def readSetOfPointerEvent() = readCard32().asSetOfPointerEvent
  def readSetOfDeviceEvent() = readCard32().asSetOfDeviceEvent

  def readCard8() = readUInt8()
  def readCard8(n: Int) = readUInt8(n)
  def readCard16() = readUInt16()
  def readCard16(n: Int) = readUInt16(n)
  def readCard32() = readUInt32()
  def readBitmask() = readCard32()
  def readWindow() = readCard32()
  def readPixmap() = readCard32()
  def readCursor() = readCard32()
  def readFont() = readCard32()
  def readGContext() = readCard32()
  def readColormap() = readCard32()
  def readDrawable() = readCard32()
  def readFontable() = readCard32()
  def readVisualID() = readCard32()
  def readTimestamp() = readCard32()
  def readKeycode() = readCard8()
  def readButton() = readCard8()
}

class BinaryInputStreamLSB(override val inputStream: InputStream) extends BinaryInputStream(inputStream) {
  override def readInt8(n: Int): Int8 = {
    val value = readInt8()
    skip(n - 1)
    value
  }

  override def readUInt8(n: Int): UInt8 = {
    val value = readUInt8()
    skip(n - 1)
    value
  }

  override def readInt16(n: Int): Int16 = {
    val value = readInt16()
    skip(n - 2)
    value
  }

  override def readUInt16(n: Int): UInt16 = {
    val value = readUInt16()
    skip(n - 2)
    value
  }


  override def readBool(n: Int): Bool = {
    val value = readBoolean()
    skip(n - 1)
    Bool(value)
  }


  override def readInt16() = Int16(this.readShort()).swapBytes
  override def readUInt16() = UInt16(this.readShort()).swapBytes

  override def readInt32() = Int32(this.readInt()).swapBytes
  override def readUInt32() = UInt32(this.readInt()).swapBytes
}

class BinaryInputStreamMSB(override val inputStream: InputStream) extends BinaryInputStream(inputStream) {
  override def readInt8(n: Int): Int8 = {
    skip(n - 1)
    readInt8()
  }

  override def readUInt8(n: Int): UInt8 = {
    skip(n - 1)
    readUInt8()
  }

  override def readInt16(n: Int): Int16 = {
    skip(n - 2)
    readInt16()
  }

  override def readUInt16(n: Int): UInt16 = {
    skip(n - 2)
    readUInt16()
  }

  override def readBool(n: Int): Bool = {
    skip(n - 1)
    readBool()
  }

  override def readInt16() = Int16(this.readShort())
  override def readUInt16() = UInt16(this.readShort())

  override def readInt32() = Int32(this.readInt())
  override def readUInt32() = UInt32(this.readInt())
}

