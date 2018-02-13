package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage
import java.lang.Math._

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.service.{ImageReconstructionService, PixelLocatorService}
import scaldi.{Injectable, Injector}

/**
  * Created by jm186111 on 12/02/2018.
  */
class AvgBlurFilterTransformer(radius:Int)(implicit in:Injector) extends Injectable with ImageTransformer{
  require(radius > 0)

  val pixelLocatorService = inject[PixelLocatorService]

  def getNormalizedAvgValue(pixelOfInterest:ColoredPixel, surroundingPixels:List[ColoredPixel]):ColoredPixel ={
    val pixelRGBValues = surroundingPixels
      .map(pixel =>
        (pixel.red / surroundingPixels.size, pixel.green / surroundingPixels.size, pixel.blue / surroundingPixels.size))
      .reduceLeft((px1,px2) => ((px1._1 + px2._1), (px1._2 + px2._2), (px1._3 + px2._3)))

    new ColoredPixel(pixelRGBValues._1, pixelRGBValues._2, pixelRGBValues._3, pixelOfInterest.x, pixelOfInterest.y)
  }

  def transform(img:BufferedImage):BufferedImage = {
    val imgBlurred = getImageCanvas(img)
    for(x <- 0 until img.getWidth();
        y <- 0 until img.getHeight;
        pixelOfInterest = new ColoredPixel(img, x, y);
        surroundingPixels = pixelLocatorService.extractSurroundingPixels(img, radius, pixelOfInterest);
        avgPixel = getNormalizedAvgValue(pixelOfInterest, surroundingPixels)
    ) yield imgBlurred.setRGB(x, y, avgPixel.rgbColor)

    imgBlurred
  }
}
