package com.smartrural.estimator.transformer

import org.opencv.core.{Mat, Size}
import org.opencv.imgproc.Imgproc

/**
  * Created by jm186111 on 12/02/2018.
  */
class AvgBlurFilterTransformer(radius:Int) extends ImageTransformer{

  override def applyTransform(img:Mat):Mat = {
    Imgproc.blur(img, img, new Size(radius, radius))
    img
  }
}
