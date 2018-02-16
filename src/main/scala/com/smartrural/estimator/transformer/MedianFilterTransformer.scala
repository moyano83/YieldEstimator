package com.smartrural.estimator.transformer

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
  * Created by jm186111 on 12/02/2018.
  */
class MedianFilterTransformer(radius:Int) extends ImageTransformer{

  override def applyTransform(img:Mat):Mat = {
    val dst = new Mat
    Imgproc.medianBlur(img, dst, radius)
    dst
  }
}
