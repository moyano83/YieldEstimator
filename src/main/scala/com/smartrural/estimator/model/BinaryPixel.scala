package com.smartrural.estimator.model

/**
  * Class that models a pixel in the image
  * @param isVoid boolean indicating if the pixel contains a value
  * @param row row position
  * @param col column position
  */
case class BinaryPixel(isVoid:Boolean, row:Int, col:Int) {
  /**
    * boolean indicating ig the pixel is not void
    */
  val isNotVoid = !isVoid
  /**
    * @inheritdoc
    */
  override def hashCode(): Int = row * col
  /**
    * @inheritdoc
    */
  override def canEqual(that: Any): Boolean = that match{
    case BinaryPixel(_, _, _) => true
    case _ => false
  }
  /**
    * @inheritdoc
    */
  override def equals(obj: scala.Any): Boolean = canEqual(obj) &&
    obj.asInstanceOf[BinaryPixel].row == this.row &&
    obj.asInstanceOf[BinaryPixel].col == this.col
}
