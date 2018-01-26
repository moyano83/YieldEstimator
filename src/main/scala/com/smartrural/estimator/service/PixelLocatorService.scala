package com.smartrural.estimator.service

import java.awt.image.BufferedImage
import java.lang.Math._

import com.smartrural.estimator.AppConstants._
import com.smartrural.estimator.model.{PixelCoordinates, RGBPixel}

class PixelLocatorService(image:BufferedImage) {

  val imageWidth = image.getWidth

  val imageHeight = image.getHeight

  def findSurroundingClusterPixels(radius:Int):Set[PixelCoordinates] =
    (for {
      i <- 0 until imageHeight;
      j <- 0 until imageWidth;
      if isCluster(image.getRGB(i, j))
    } yield extractGrapePixelSurroundings(radius, PixelCoordinates(i,j))).flatten.toSet

  def extractGrapePixelSurroundings(radius:Int, pixelCoordinates:PixelCoordinates):List[PixelCoordinates] =
    (for {
      i <- max(pixelCoordinates.y - radius, MinCoordinateValue) to min(pixelCoordinates.y + radius, imageHeight - 1);
      j <- max(pixelCoordinates.x - radius, MinCoordinateValue) to min(pixelCoordinates.x + radius, imageWidth - 1);
      if !isCluster(image.getRGB(i, j))
    } yield PixelCoordinates(i, j)).toList

  private def isCluster(pixel:Int):Boolean = RGBPixel(pixel) != VoidRGB

}

object PixelLocatorService{
  def apply(image: BufferedImage): PixelLocatorService = new PixelLocatorService(image)
}
