package com.smartrural.estimator.util

import com.smartrural.estimator.model.RGBPixel

object AppConstants {
  /**
    * Value for the min coordinate to use in iterations
    */
  val ZeroCoordinate = 0
  /**
    * Value of an unset pixel
    */
  val VoidRGB = RGBPixel(0)
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
    * JPG Format
    */
  val JpgFormat = "jpg"
  /**
    * PNG format
    */
  val PngFormat = "png"
}
