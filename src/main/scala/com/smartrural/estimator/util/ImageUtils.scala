package com.smartrural.estimator.util

import java.awt.image.BufferedImage
import java.lang.Math.{max, min}

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.AppConstants.ZeroCoordinate

/**
  * Created by jm186111 on 14/02/2018.
  */
object ImageUtils {
  def extractSurroundingPixels(img:BufferedImage, radius:Int, pixel:ColoredPixel):List[ColoredPixel] =
    (for { x <- max(pixel.x - radius, ZeroCoordinate) to min(pixel.x + radius, img.getWidth - 1);
           y <- max(pixel.y - radius, ZeroCoordinate) to min(pixel.y + radius, img.getHeight - 1)
    } yield new ColoredPixel(img, x, y)).toList
}
