package com.smartrural.estimator.service.impl

import java.awt.image.BufferedImage
import java.io.File
import java.lang.Math._

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.service.{FileManagerService, PixelLocatorService}
import com.smartrural.estimator.util.AppConstants._
import scaldi.{Injectable, Injector}

class ImagePixelLocatorService(implicit inj:Injector) extends PixelLocatorService with Injectable{

  val fileManager = inject[FileManagerService]

  override def findSurroundingClusterPixels(image:BufferedImage, radius:Int):Set[ColoredPixel] = {
    (for {
      x <- 0 until image.getWidth;
      y <- 0 until image.getHeight;
      pixel = new ColoredPixel(image, x, y)
      if !pixel.isVoid
    } yield extractSurroundingPixels(image, radius, pixel)).flatten.toSet.filter(!_.isVoid())
  }

}
