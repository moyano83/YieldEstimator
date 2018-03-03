package com.smartrural.estimator.model

/**
  * Class that models a pixel in the image
  * @param rgbColor the rgb of the pixel
  * @param row row position
  * @param col column position
  */
case class ColoredPixel(rgbColor:Int, row:Int, col:Int) {
  /**
    * Secondary constructor
    * @param red the red value of the rgb channel
    * @param green the green value of the rgb channel
    * @param blue the blue value of the rgb channel
    * @param row row position
    * @param col column position
    */
  def this(red:Int, green:Int, blue:Int, row:Int, col:Int) {
    this(red * 65536 + green * 256 + blue, row, col)
  }

  /**
    * Red channel value
    */
  val red = (rgbColor & 0xff0000) / 65536
  /**
    * Green channel value
    */
  val green = (rgbColor & 0xff00) / 256
  /**
    * Blue channel value
    */
  val blue = (rgbColor & 0xff)

  /**
    * Method that returns true if the pixel does have a void value on the RGB channel
    * @return true if void
    */
  def isVoid():Boolean = (red == 0) && (green ==0) && (blue == 0)
  /**
    * Method that returns true if the pixel does not have a void value on the RGB channel
    * @return true if not void
    */
  def isNotVoid():Boolean = !isVoid()

  /**
    * @inheritdoc
    */
  override def hashCode(): Int = blue
  /**
    * @inheritdoc
    */
  override def canEqual(that: Any): Boolean = that match{
    case ColoredPixel(_, _, _) => true
    case _ => false
  }
  /**
    * @inheritdoc
    */
  override def equals(obj: scala.Any): Boolean = canEqual(obj) &&
    obj.asInstanceOf[ColoredPixel].red == this.red &&
    obj.asInstanceOf[ColoredPixel].green == this.green &&
    obj.asInstanceOf[ColoredPixel].blue == this.blue &&
    obj.asInstanceOf[ColoredPixel].row == this.row &&
    obj.asInstanceOf[ColoredPixel].col == this.col
}
