package com.smartrural.estimator.transformer

import java.awt.image.{BufferedImage, ConvolveOp, Kernel}
import java.lang.Math._

/**
  * Created by jm186111 on 13/02/2018.
  */
class GaussianFilterTransformer(radius:Int = 5, sigma:Double = 1.0) extends ImageTransformer {

  private def getGaussValue(x:Int, y:Int):Float =
    ((1.0 / 2 * PI * pow(sigma,2)) * pow(E, -(pow(x,2) + pow(y,2)) / (2 * pow(sigma,2)))).toFloat

  /**
    * Kernel to apply to the image
    */
  private val gaussianConvolveOp = {
    val matrix = for(y <- -radius to radius;
        x <- -radius to radius
    ) yield getGaussValue(y,x)
    val normalizedMatrix = matrix.map(_ / matrix.sum).toArray
    new ConvolveOp(new Kernel(radius, radius, normalizedMatrix))
  }

  /**
    * Transform an imagen according to the internal implementation of the Transformer
    *
    * @param img the image to transform
    * @return the transformed image
    */
  override def transform(img: BufferedImage): BufferedImage = gaussianConvolveOp.filter(img, getImageCanvas(img))

}
