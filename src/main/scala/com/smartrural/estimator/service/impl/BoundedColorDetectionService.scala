package com.smartrural.estimator.service.impl

import java.awt.Color

import com.smartrural.estimator.model.RGBPixel
import com.smartrural.estimator.service.ColorDetectionService

/**
  * Created by jm186111 on 05/02/2018.
  */
class BoundedColorDetectionService(hue:Range, saturation:Range, value:Range) extends ColorDetectionService {

  /**
    * Returns a Boolean indicating if the pixel is within the configured HSB color value
    *
    * @param pixel the rgb value
    * @return true if it is inside the range
    */
  override def isWithinRange(pixel: RGBPixel): Boolean = {
    val hsbValue = getHSBColor(pixel)
    isValueOnRange(hue, hsbValue(0)) && isValueOnRange(saturation, hsbValue(1)) && isValueOnRange(value, hsbValue(2))
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
  private def isValueOnRange(range:Range, value:Float):Boolean = range.inclusive.contains(value)

}
