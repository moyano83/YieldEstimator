package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.ImageUtils

/**
  * Created by jm186111 on 12/02/2018.
  */
class MedianFilterTransformer(radius:Int) extends ImageTransformer{

  def filterFunction(surroundingPixels:List[ColoredPixel]):ColoredPixel = {
    val medianValue = surroundingPixels.size / 2 + 1
    val medianPixel = surroundingPixels(medianValue)
    val redList = surroundingPixels.map(_.red).sorted
    val greenList = surroundingPixels.map(_.green).sorted
    val blueList = surroundingPixels.map(_.blue).sorted

    new ColoredPixel(redList(medianValue), greenList(medianValue), blueList(medianValue), medianPixel.x, medianPixel.y)
  }

  override def applyTransform(img:BufferedImage):BufferedImage = {
    val imgBlurred = getImageCanvas(img, false)
    for(x <- 0 until img.getWidth();
        y <- 0 until img.getHeight;
        pixelOfInterest = new ColoredPixel(img, x, y);
        surroundingPixels = ImageUtils.extractSurroundingPixels(img, radius, pixelOfInterest);
        medianPixel = filterFunction(surroundingPixels)
    ) yield {
      logger.debug(s"Procesing pixel coordinate (${x}, ${y})")
      imgBlurred.setRGB(x, y, medianPixel.rgbColor)
    }

    imgBlurred
  }
}
