package com.smartrural.estimator.model

import java.awt.Color
import java.awt.image.BufferedImage

import com.smartrural.estimator.util.AppConstants

case class ColoredPixel(rgbColor:Int, x:Int, y:Int) {

  def this(img:BufferedImage, x:Int, y:Int) {
    this(img.getRGB(x,y), x, y)
  }

  def this(red:Int, green:Int, blue:Int, x:Int, y:Int) {
    this(red * 65536 + green * 256 + blue, x, y)
  }

  def this(hsvArray:Array[Float], x:Int, y:Int) {
    this(Color.HSBtoRGB(hsvArray(0), hsvArray(1), hsvArray(2)), x, y)
  }

  val red = (rgbColor & 0xff0000) / 65536

  val green = (rgbColor & 0xff00) / 256

  val blue = (rgbColor & 0xff)

  private val hsb = Color.RGBtoHSB(red, green, blue, null)

  def getHue = hsb(0)

  def getSaturation = hsb(1)

  def getValue = hsb(2)

  def isVoid():Boolean = (red == 0) && (green ==0) && (blue == 0)

  override def hashCode(): Int = blue

  override def canEqual(that: Any): Boolean = that match{
    case ColoredPixel(_, _, _) => true
    case _ => false
  }

  override def equals(obj: scala.Any): Boolean = canEqual(obj) &&
    obj.asInstanceOf[ColoredPixel].red == this.red &&
    obj.asInstanceOf[ColoredPixel].green == this.green &&
    obj.asInstanceOf[ColoredPixel].blue == this.blue &&
    obj.asInstanceOf[ColoredPixel].x == this.x &&
    obj.asInstanceOf[ColoredPixel].y == this.y
}
