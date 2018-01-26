package com.smartrural.estimator.model

case class RGBPixel(color:Int) {
  val red = (color & 0xff0000) / 65536
  val green = (color & 0xff00) / 256
  val blue = (color & 0xff)

  override def hashCode(): Int = blue

  override def canEqual(that: Any): Boolean = that match{
    case RGBPixel(_) => true
    case _ => false
  }

  override def equals(obj: scala.Any): Boolean = canEqual(obj) &&
    obj.asInstanceOf[RGBPixel].red == this.red &&
    obj.asInstanceOf[RGBPixel].green == this.green &&
    obj.asInstanceOf[RGBPixel].blue == this.blue
}
