package com.tuvistavie.xserver.protocol.atoms

abstract class BaseAtom(val value: Int)

case class Primary extends BaseAtom(1)
case class Secondary extends BaseAtom(2)
case class Arc extends BaseAtom(3)
case class Atom extends BaseAtom(4)
case class Bitmap extends BaseAtom(5)
case class Cardinal extends BaseAtom(6)
case class Colormap extends BaseAtom(7)
case class Cursor extends BaseAtom(8)
case class CutBuffer0 extends BaseAtom(9)
case class CutBuffer1 extends BaseAtom(10)
case class CutBuffer2 extends BaseAtom(11)
case class CutBuffer3 extends BaseAtom(12)
case class CutBuffer4 extends BaseAtom(13)
case class CutBuffer5 extends BaseAtom(14)
case class CutBuffer6 extends BaseAtom(15)
case class CutBuffer7 extends BaseAtom(16)
case class Drawable extends BaseAtom(17)
case class Font extends BaseAtom(18)
case class Integer extends BaseAtom(19)
case class PixmapAtom extends BaseAtom(20)
case class Point extends BaseAtom(21)
case class Rectangle extends BaseAtom(22)
case class ResourceManager extends BaseAtom(23)
case class RgbColorMap extends BaseAtom(24)
case class RgbBestMap extends BaseAtom(25)
case class RgbBlueMap extends BaseAtom(26)
case class RgbDefaultMap extends BaseAtom(27)
case class RgbGrayMap extends BaseAtom(28)
case class RgbGreenMap extends BaseAtom(29)
case class RgbRedMap extends BaseAtom(30)
case class String extends BaseAtom(31)
case class Visualid extends BaseAtom(32)
case class Window extends BaseAtom(33)
case class WmCommand extends BaseAtom(34)
case class WmHints extends BaseAtom(35)
case class WmClientMachine extends BaseAtom(36)
case class WmIconName extends BaseAtom(37)
case class WmIconSize extends BaseAtom(38)
case class WmName extends BaseAtom(39)
case class WmNormalHints extends BaseAtom(40)
case class WmSizeHints extends BaseAtom(41)
case class WmZoomHints extends BaseAtom(42)
case class MinSpace extends BaseAtom(43)
case class NormSpace extends BaseAtom(44)
case class MaxSpace extends BaseAtom(45)
case class EndSpace extends BaseAtom(46)
case class SuperscriptX extends BaseAtom(47)
case class SuperscriptY extends BaseAtom(48)
case class SubscriptX extends BaseAtom(49)
case class SubscriptY extends BaseAtom(50)
case class UnderlinePosition extends BaseAtom(51)
case class UnderlineThickness extends BaseAtom(52)
case class StrikeoutAscent extends BaseAtom(53)
case class StrikeoutDescent extends BaseAtom(54)
case class ItalicAngle extends BaseAtom(55)
case class XHeight extends BaseAtom(56)
case class QuadWidth extends BaseAtom(57)
case class Weight extends BaseAtom(58)
case class PointSize extends BaseAtom(59)
case class Resolution extends BaseAtom(60)
case class Copyright extends BaseAtom(61)
case class Notice extends BaseAtom(62)
case class FontName extends BaseAtom(63)
case class FamilyName extends BaseAtom(64)
case class FullName extends BaseAtom(65)
case class CapHeight extends BaseAtom(66)
case class WmClass extends BaseAtom(67)
case class WmTransientFor extends BaseAtom(68)
