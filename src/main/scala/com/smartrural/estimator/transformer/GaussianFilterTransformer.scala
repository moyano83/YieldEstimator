package com.smartrural.estimator.transformer

import org.opencv.core.{Mat, Size}
import org.opencv.imgproc.Imgproc

/**
  * Created by jm186111 on 13/02/2018.
  */
class GaussianFilterTransformer(radius:Int = 5, sigma:Double = 1.0) extends ImageTransformer{

  override def applyTransform(img:Mat):Mat = {
    val s = new Size(radius,radius)
    Imgproc.GaussianBlur(img, img, s, sigma)
    img
  }

}
