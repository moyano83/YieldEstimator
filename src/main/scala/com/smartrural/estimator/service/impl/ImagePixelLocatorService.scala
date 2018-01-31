package com.smartrural.estimator.service.impl

import java.awt.image.BufferedImage
import java.io.File
import java.lang.Math._
import javax.imageio.ImageIO

import com.smartrural.estimator.model.{PixelCoordinates, RGBPixel}
import com.smartrural.estimator.service.PixelLocatorService
import com.smartrural.estimator.util.AppConstants._

class ImagePixelLocatorService extends PixelLocatorService{

  override def findSurroundingClusterPixels(image:File, radius:Int):Set[PixelCoordinates] = {
    val bufferedImage = ImageIO.read(image)
    (for {
      i <- 0 until bufferedImage.getWidth;
      j <- 0 until bufferedImage.getHeight;
      if isCluster(bufferedImage.getRGB(i, j))
    } yield extractGrapePixelSurroundings(bufferedImage, radius, PixelCoordinates(i, j))).flatten.toSet
  }
  def extractGrapePixelSurroundings(image:BufferedImage,
                                    radius:Int,
                                    pixelCoordinates:PixelCoordinates):List[PixelCoordinates] =
    (for {
      x <- max(pixelCoordinates.x - radius, ZeroCoordinate) to min(pixelCoordinates.x + radius, image.getWidth - 1);
      y <- max(pixelCoordinates.y - radius, ZeroCoordinate) to min(pixelCoordinates.y + radius, image.getHeight - 1);
      if !isCluster(image.getRGB(x, y))
    } yield PixelCoordinates(x, y)).toList

  private def isCluster(pixel:Int):Boolean = RGBPixel(pixel) != VoidRGB

}
