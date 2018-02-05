package com.smartrural.estimator.service.impl

import java.awt.image.BufferedImage
import java.io.File
import java.lang.Math._

import com.smartrural.estimator.model.{PixelCoordinates, RGBPixel}
import com.smartrural.estimator.service.{FileManagerService, PixelLocatorService}
import com.smartrural.estimator.util.AppConstants._
import scaldi.{Injectable, Injector}

class ImagePixelLocatorService(implicit inj:Injector) extends PixelLocatorService with Injectable{

  val fileManager = inject[FileManagerService]

  override def findSurroundingClusterPixels(image:File, radius:Int):Set[PixelCoordinates] = {
    val bufferedImage = fileManager.readImage(image)
    (for {
      i <- 0 until bufferedImage.getWidth;
      j <- 0 until bufferedImage.getHeight;
      if !isVoidRGB(bufferedImage.getRGB(i, j))
    } yield extractGrapePixelSurroundings(bufferedImage, radius, PixelCoordinates(i, j))).flatten.toSet
  }
  def extractGrapePixelSurroundings(image:BufferedImage,
                                    radius:Int,
                                    pixelCoordinates:PixelCoordinates):List[PixelCoordinates] =
    (for {
      x <- max(pixelCoordinates.x - radius, ZeroCoordinate) to min(pixelCoordinates.x + radius, image.getWidth - 1);
      y <- max(pixelCoordinates.y - radius, ZeroCoordinate) to min(pixelCoordinates.y + radius, image.getHeight - 1);
      if isVoidRGB(image.getRGB(x, y))
    } yield PixelCoordinates(x, y)).toList

  private def isVoidRGB(pixel:Int):Boolean = RGBPixel(pixel) == VoidRGB

}
