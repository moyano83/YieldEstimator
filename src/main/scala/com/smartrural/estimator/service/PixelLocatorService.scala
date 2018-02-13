package com.smartrural.estimator.service

import java.awt.image.BufferedImage
import java.lang.Math.{max, min}

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.AppConstants.ZeroCoordinate

/**
  * Created by jm186111 on 31/01/2018.
  */
trait PixelLocatorService {

  def findSurroundingClusterPixels(image:BufferedImage, radius:Int):Set[ColoredPixel]

  def extractSurroundingPixels(img:BufferedImage, radius:Int, pixel:ColoredPixel):List[ColoredPixel] =
    (for { x <- max(pixel.x - radius, ZeroCoordinate) to min(pixel.x + radius, img.getWidth - 1);
           y <- max(pixel.y - radius, ZeroCoordinate) to min(pixel.y + radius, img.getHeight - 1)
    } yield new ColoredPixel(img, x, y)).toList
}
