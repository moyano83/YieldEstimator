package com.smartrural.estimator.util

import com.smartrural.estimator.model.RGBPixel

object AppConstants {
  /**
    * Value for the min coordinate to use in iterations
    */
  val MinCoordinateValue = 0
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
}
