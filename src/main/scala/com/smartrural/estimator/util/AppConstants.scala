package com.smartrural.estimator.util

import com.smartrural.estimator.model.BinaryPixel

object AppConstants {
  /**
    * RGB white color
    */
  val WhiteColor = Array(255.toByte, 255.toByte, 255.toByte)
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
  val VoidRGB = BinaryPixel(true, 0, 0)
  /**
    * Name of the files holding information about the bounding boxes
    */
  val BbBoxesFileName = "bboxes.txt"
  /**
    * Property containing the path to the patch images
    */
  val PropertyInferencesFile = "inferences.file.path"
  /**
    * Property containing the path to the filtered images
    */
  val PropertyFilteredImagePath = "filtered.image.path"
  /**
    * Property containing the path to the mask binary images
    */
  val PropertyMaskImagePath = "mask.image.path"
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
  val PropertyDestinationPath = "destination.file.path"
  /**
    * Property containing the destination path
    */
  val PropertyRadiusPixelLocator = "radius.pixel.locator"
  /**
    * Minimum value for hue
    */
  val PropertyHueMinValue = "hue.min.value"
  /**
    * Maximum value for hue
    */
  val HueMaxValue = "hue.max.value"
  /**
    * Minimum value for saturation
    */
  val PropertySaturationMinValue = "saturation.min.value"
  /**
    * Maximum value for saturation
    */
  val PropertySaturationMaxValue = "saturation.max.value"
  /**
    * Minimum value for brightness
    */
  val PropertyBrightnessMinValue = "brightness.min.value"
  /**
    * Maximum value for brightness
    */
  val PropertyBrightnessMaxValue = "brightness.max.value"
  /**
    * Gauss Sigma value property name
    */
  val PropertyGaussSigmaValue = "gauss.sigma.value"
  /**
    * Gauss number of iterations property name
    */
  val PropertyGaussIterationsValue = "gauss.iterations.value"
  /**
    * Gauss number of iterations property name
    */
  val PropertySampleImageHistogram = "sample.histogram.image"
  /**
    * JPG Format
    */
  val FormatJpg = "jpg"
  /**
    * PNG format
    */
  val FormatPng = "png"
  /**
    * BMP format
    */
  val FormatBmp = "BMP"
  /**
    * txt extension
    */
  val TextExtension = "txt"
}
