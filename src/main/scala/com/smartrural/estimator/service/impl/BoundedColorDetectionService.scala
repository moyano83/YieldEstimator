package com.smartrural.estimator.service.impl

import java.awt.Color

import com.smartrural.estimator.model.RGBPixel
import com.smartrural.estimator.service.ColorDetectionService

/**
  * Created by jm186111 on 05/02/2018.
  */
class BoundedColorDetectionService(hue:Range, saturation:Range, value:Range) extends ColorDetectionService {

  /**
    * Saturation Normalizing Factor
    */
  val HueNormalizingFactor = 360
  /**
    * Saturation Normalizing Factor
    */
  val SaturationNormalizingFactor = 100
  /**
    * Value Normalizing Factor
    */
  val ValueNormalizingFactor = 100
  /**
    * Returns a Boolean indicating if the pixel is within the configured HSB color value
    *
    * @param pixel the rgb value
    * @return true if it is inside the range
    */
  override def isWithinRange(pixel: RGBPixel): Boolean = {
    val hsbValue = getHSBColor(pixel)
    val hueVal = hsbValue(0)
    val satVal = hsbValue(1)
    val valueVal = hsbValue(1)

    isValueOnRange(hue, HueNormalizingFactor, hueVal) &&
      isValueOnRange(saturation, SaturationNormalizingFactor, satVal) &&
      isValueOnRange(value, ValueNormalizingFactor, valueVal)
  }

  /**
    * Converts the pixel from RGB to HSB
    * @param pixel the pixel to convert
    * @return The HSB color value
    */
  private def getHSBColor(pixel: RGBPixel):Array[Float] = Color.RGBtoHSB(pixel.red, pixel.green, pixel.blue, null)

  /**
    * Methods that check that a certain value is within a certain range
    * @param range a tuple with the max and min values for the range
    * @param value the value to check
    * @return true if it is within range
    */
  private def isValueOnRange(range:Range, normalizingFactor:Int, value:Float):Boolean = {
    val normalizedValue = (normalizingFactor * value).toInt
    range.min <= normalizedValue && normalizedValue <= range.max
  }

}
