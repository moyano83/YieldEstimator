package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.ImageUtils

/**
  * Created by jm186111 on 12/02/2018.
  */
class AvgBlurFilterTransformer(radius:Int) extends ImageTransformer{


  def filterFunction(surroundingPixels:List[ColoredPixel]):ColoredPixel ={
    val medianPixel = surroundingPixels(surroundingPixels.size / 2 + 1)
    val size = surroundingPixels.size
    val (r,g,b) = surroundingPixels
      .map(pixel => (pixel.red, pixel.green, pixel.blue))
      .reduceLeft((px1,px2) => (px1._1 + px2._1, px1._2 + px2._2, px1._3 + px2._3))
    new ColoredPixel(r / size, g / size, b / size, medianPixel.x, medianPixel.y)
  }

  override def applyTransform(img:BufferedImage):BufferedImage = {
    val imgBlurred = getImageCanvas(img, false)
    for(x <- 0 until img.getWidth();
        y <- 0 until img.getHeight;
        pixelOfInterest = new ColoredPixel(img, x, y);
        surroundingPixels = ImageUtils.extractSurroundingPixels(img, radius, pixelOfInterest);
        avgPixel = filterFunction(surroundingPixels)
    ) yield {
      logger.debug(s"Procesing pixel coordinate (${x}, ${y})");
      imgBlurred.setRGB(x, y, avgPixel.rgbColor)
    }

    imgBlurred
  }
}
