package com.smartrural.estimator.transformer.impl

import java.io.File

import com.smartrural.estimator.transformer.ImageTransformer
import org.opencv.core.{Mat, Size}
import org.opencv.imgproc.Imgproc

/**
  * Gaussian filter transformer
  * @param radius the radius of the transformation
  * @param sigma the standard deviation of the transformation
  */
class GaussianFilterTransformer(radius:Int = 5, sigma:Double = 1.0) extends ImageTransformer{
  /**
    * @inheritdoc
    */
  override def transform(matFile:File, img:Mat):Mat = {
    val s = new Size(radius,radius)
    Imgproc.GaussianBlur(img, img, s, sigma)
    img
  }

}
