package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage
import java.lang.Math._

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.service.{FileManagerService, PixelLocatorService}
import org.slf4j.LoggerFactory
import scaldi.{Injectable, Injector}

/**
  * Created by jm186111 on 13/02/2018.
  */
class GaussianFilterTransformer(radius:Int = 5, sigma:Double = 1.0, iterations:Int = 1)
                               (implicit inj:Injector) extends ImageTransformer with Injectable {

  val logger = LoggerFactory.getLogger(getClass)

  val pixelService = inject[PixelLocatorService]

  val fileManagerService = inject[FileManagerService]

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

  private def getGaussFilteredPixelWithRGB(x:Int, y:Int, colouredPixels: List[ColoredPixel]):ColoredPixel = {
    val (r,g,b) = colouredPixels.zip(gaussianNormalisedMatrix).map{
      case(pixel, factor) => (pixel.red * factor,  pixel.green * factor, pixel.blue * factor)
    }.fold((0f,0f,0f))((a,b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3))
    new ColoredPixel(r.toInt, g.toInt, b.toInt, x, y)
  }

  private def getGaussFilteredPixelWithHSB(colouredPixels: List[ColoredPixel]):ColoredPixel = {
    val medianPixel = colouredPixels(colouredPixels.size / 2 + 1)
    val (h,s,v) = colouredPixels.zip(gaussianNormalisedMatrix).map{
      case(pixel, factor) => (pixel.getHue * factor,  pixel.getSaturation * factor, pixel.getValue * factor)
    }.fold((0f,0f,0f))((a,b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3))
    new ColoredPixel(Array(h,s,v), medianPixel.x, medianPixel.y)
  }

  private def applyGaussianFilter(img:BufferedImage):BufferedImage = {
    val dstImg = getImageCanvas(img, true)
    for(x<- radius to img.getWidth-radius;
        y<- radius to img.getHeight -radius;
        originalPixel = new ColoredPixel(img, x, y);
        px = getGaussFilteredPixelWithHSB(pixelService.extractSurroundingPixels(img, radius, originalPixel))
    ) yield fileManagerService.writePixel(px, dstImg)
    dstImg
  }

  /**
    * Transform an imagen according to the internal implementation of the Transformer
    *
    * @param img the image to transform
    * @return the transformed image
    */
  override def transform(img: BufferedImage): BufferedImage = {
    var imgDestination = img
    for(i <- 0 until iterations){
      logger.info(s"Gaussian filter iteration [${}zZ]")
      imgDestination = applyGaussianFilter(imgDestination)
    }
    imgDestination
  }

}
