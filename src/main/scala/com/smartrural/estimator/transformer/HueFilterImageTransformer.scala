package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage

import com.smartrural.estimator.model.ColoredPixel

/**
  * Created by jm186111 on 05/02/2018.
  */
class HueFilterImageTransformer(hue:Range, saturation:Range, value:Range) extends ImageTransformer {

  override val filterName = "HueFilter"
  /**
    * Degrees Normalizing Factor
    */
  val DegreesNormalizingFactor = 360
  /**
    * percentage Normalizing Factor
    */
  val PercentageNormalizingFactor = 100

  override def applyTransform(img:BufferedImage): BufferedImage = {
    val imgFiltered = getImageCanvas(img, false)
    for(x <- 0 until img.getWidth;
        y <- 0 until img.getHeight;
        pixelOfInterest = new ColoredPixel(img, x, y) if isWithinRange(pixelOfInterest)
    ) yield imgFiltered.setRGB(x, y, pixelOfInterest.rgbColor)
    imgFiltered
  }
  /**
    * Returns a Boolean indicating if the pixel is within the configured HSB color value
    *
    * @param pixel the rgb value
    * @return true if it is inside the range
    */
  private def isWithinRange(pixel: ColoredPixel): Boolean =
    isValueOnRange(hue, DegreesNormalizingFactor, pixel.getHue) &&
      isValueOnRange(saturation, PercentageNormalizingFactor, pixel.getSaturation) &&
      isValueOnRange(value, PercentageNormalizingFactor, pixel.getValue)

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
