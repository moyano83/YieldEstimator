package com.smartrural.estimator.util

import com.smartrural.estimator.model.ColoredPixel

object AppConstants {
  /**
    * RGB red color
    */
  val RedColor = Array(255.toByte, 0.toByte, 0.toByte)
  /**
    * RGB unset color
    */
  val VoidColor = Array(0.toByte, 0.toByte, 0.toByte)
  /**
    * Value for the min coordinate to use in iterations
    */
  val ZeroCoordinate = 0
  /**
    * Value of an unset pixel in the position 0,0
    */
  val VoidRGB = ColoredPixel(0, 0, 0)
  /**
    * Name of the files holding information about the bounding boxes
    */
  val BbBoxesFileName = "bboxes.txt"
  /**
    * Property containing the path to the patch images
    */
  val PropertyPatchesPath = "patch.image.path"
  /**
    * Property containing the path to the original images
    */
  val PropertyOriginalImagePath = "original.image.path"
  /**
    * Property containing the path to the bboxes files
    */
  val PropertyBBoxesPath = "bbox.file.path"
  /**
    * Property containing the destination path
    */
  val DestinationPath = "destination.file.path"
  /**
    * Property containing the destination path
    */
  val BinaryImagesPath = "binary.images.path"
  /**
    * Property containing the destination path
    */
  val RadiusPixelLocator = "radius.pixel.locator"
  /**
    * Minimum value for hue
    */
  val HueMinValue = "hue.min.value"
  /**
    * Maximum value for hue
    */
  val HueMaxValue = "hue.max.value"
  /**
    * Minimum value for saturation
    */
  val SaturationMinValue = "saturation.min.value"
  /**
    * Maximum value for saturation
    */
  val SaturationMaxValue = "saturation.max.value"
  /**
    * Minimum value for brightness
    */
  val BrightnessMinValue = "brightness.min.value"
  /**
    * Maximum value for brightness
    */
  val BrightnessMaxValue = "brightness.max.value"
  /**
    * Gauss Sigma value property name
    */
  val GaussSigmaValue = "gauss.sigma.value"
  /**
    * Gauss number of iterations property name
    */
  val GaussIterationsValue = "gauss.iterations.value"
  /**
    * JPG Format
    */
  val JpgFormat = "jpg"
  /**
    * PNG format
    */
  val PngFormat = "png"
  /**
    * BMP format
    */
  val BmpFormat = "BMP"
}
