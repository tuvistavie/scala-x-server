package com.tuvistavie.xserver.protocol.reply

import com.tuvistavie.xserver.io._
import com.tuvistavie.xserver.protocol.types._

abstract class Reply(
  val data: Option[Card8],
  val sequenceNumber: Card16,
  val length: Card32
) {
  def write(stream: BinaryOutputStream): Unit
  def write(stream: BinaryOutputStream, values: Value*) = {
    stream.writeByte(1)
    data match {
      case Some(n) => stream.writeByte(n)
      case _ => stream.writeByte(0)
    }
    stream.writeUInt16(sequenceNumber)
    stream.writeUInt32(length)

    values.foreach(stream.writeValue _)

    if(length.value == 0) {
      stream.fill(unusedBytesNumber(values: _*))
    }
  }
  def unusedBytesNumber(values: Value*) = {
    24 - values./: (0) (_+_.byteSize)
  }
}

case class GetWindowAttributes (
  val backingStore: Card8,
  override val sequenceNumber: Card16,
  val visual: VisualID,
  val className: Card8,
  val bitGravity: BitGravity,
  val windowGravity: WindowGravity,
  val backingPlanes: Card32,
  val backingPixels: Card32,
  val saveUnder: Bool,
  val mapIsInstalled: Bool,
  val mapState: Card8,
  val overrideRedirect: Bool,
  val colorMap: Colormap,
  val allEventMasks: SetOfEvent,
  val yourEventMask: SetOfEvent,
  val doNotPropagateMask: SetOfDeviceEvent
) extends Reply(Some(backingStore), sequenceNumber, 3) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, visual, className, bitGravity, windowGravity,
      backingPlanes, backingPixels, saveUnder, mapIsInstalled,
      mapState, overrideRedirect, colorMap, allEventMasks,
      yourEventMask, doNotPropagateMask)
    stream.fill(2)
  }
}

case class GetGeometry (
  val depth: Card8,
  override val sequenceNumber: Card16,
  val root: Window,
  val x: Int16,
  val y: Int16,
  val width: Card16,
  val height: Card16,
  val borderWidth: Card16
) extends Reply(Some(depth), sequenceNumber, 0) {
  def write(stream: BinaryOutputStream) = {
    super.write(stream, root, x, y, width, height, borderWidth)
  }
}

case class QueryTree (
  override val sequenceNumber: Card16,
  val root: Window,
  val parent: Window,
  val numberOfWindows: Card16,
  val children: List[Window]
) extends Reply(None, sequenceNumber, numberOfWindows) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, root, parent, numberOfWindows)
    stream.fill(14)
    children foreach { stream.writeWindow(_) }
  }
}

case class InternAtom (
  override val sequenceNumber: Card16,
  val atom: Atom
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, atom)
  }
}

case class GetAtomName (
  override val sequenceNumber: Card16,
  val nameLength: Card16,
  val name: Str
) extends Reply(None, sequenceNumber, (nameLength.value + nameLength.padding) / 4) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, nameLength)
    stream.writeString8(name)
    stream.writePad(nameLength)
  }
}

case class GetProperty (
  val format: Card8,
  override val sequenceNumber: Card16,
  val typeName: Atom,
  val bytesAfter: Card32,
  val bytes: List[Card8]
) extends Reply(Some(format), sequenceNumber, (UInt32(bytes.length).padding + bytes.length) / 4) {
  override def write(stream: BinaryOutputStream) = {
    val lengthWithFormat: Card32 = if(format == 0) 0 else (bytes.length * 8) / format
    super.write(stream, typeName, bytesAfter, lengthWithFormat)
    if(bytes.length > 0) {
      stream.fill(12)
      stream.writeCard8List(bytes)
      stream.writePad(bytes.length)
    }
  }
}

case class ListProperties (
  override val sequenceNumber: Card16,
  val atoms: List[Atom]
) extends Reply(None, sequenceNumber, atoms.length) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, UInt32(atoms.length))
    if(atoms.length > 0) {
      stream.fill(22)
      stream.writeAtomList(atoms)
    }
  }
}

case class GetSelectionOwner (
  override val sequenceNumber: Card16,
  val owner: Window
) extends Reply(None, sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, owner)
  }
}

case class GrabPointer (
  val status: Card8,
  override val sequenceNumber: Card16
) extends Reply(Some(status), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
  }
}

case class GrabKeyboard (
  val status: Card8,
  override val sequenceNumber: Card16
) extends Reply(Some(status), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, List(): _*)
  }
}

case class QueryPointer (
  val sameScreen: Card8,
  override val sequenceNumber: Card16,
  val root: Window,
  val child: Window,
  val rootX: Int16,
  val rootY: Int16,
  val winX: Int16,
  val winY: Int16,
  val mask: SetOfKeyButMask
) extends Reply(Some(sameScreen), sequenceNumber, 0) {
  override def write(stream: BinaryOutputStream) = {
    super.write(stream, root, child, rootX, rootY, winX, winY, mask)
  }
}
