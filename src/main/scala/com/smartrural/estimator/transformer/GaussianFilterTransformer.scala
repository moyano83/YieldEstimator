package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage
import java.lang.Math._

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.ImageUtils

/**
  * Created by jm186111 on 13/02/2018.
  */
class GaussianFilterTransformer(radius:Int = 5,
                                sigma:Double = 1.0,
                                override val iterations:Int = 1) extends ImageTransformer{


  private def getGaussValue(x:Int, y:Int):Float =
    ((1.0 / 2 * PI * pow(sigma,2)) * pow(E, -(pow(x,2) + pow(y,2)) / (2 * pow(sigma,2)))).toFloat

  /**
    * Kernel to apply to the image
    */
  private val gaussianNormalisedMatrix = {
    val matrix = for(y <- -radius to radius;
        x <- -radius to radius
    ) yield getGaussValue(y,x)
    matrix.map(_ / matrix.sum).toList
  }

  private def filterFunction(colouredPixels: List[ColoredPixel]):ColoredPixel = {
    val medianPixel = colouredPixels(colouredPixels.size / 2 + 1)
    val (r,g,b) = colouredPixels.zip(gaussianNormalisedMatrix).map{
      case(pixel, factor) => (pixel.red * factor,  pixel.green * factor, pixel.blue * factor)
    }.reduceLeft((a,b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3))
    new ColoredPixel(r.toInt, g.toInt, b.toInt, medianPixel.x, medianPixel.y)
  }

  override def applyTransform(img:BufferedImage):BufferedImage = {
    val dstImg = getImageCanvas(img, true)
    for(x<- radius to img.getWidth-radius;
        y<- radius to img.getHeight -radius;
        originalPixel = new ColoredPixel(img, x, y);
        px = filterFunction(ImageUtils.extractSurroundingPixels(img, radius, originalPixel))
    ) yield {
      logger.debug(s"Procesing pixel coordinate (${x}, ${y})")
      dstImg.setRGB(x,y, px.rgbColor)
    }
    dstImg
  }

}
