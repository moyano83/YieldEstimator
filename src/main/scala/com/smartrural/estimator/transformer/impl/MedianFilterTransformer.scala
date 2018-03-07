package com.smartrural.estimator.transformer.impl

import com.smartrural.estimator.transformer.ImageTransformer
import com.smartrural.estimator.util.ImageUtils.getMat
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
  * Median filter transformer
  * @param radius the radius of the transformation
  */
class MedianFilterTransformer(radius:Int) extends ImageTransformer{
  /**
    * @inheritdoc
    */
  override def transform(img:Mat):Mat = {
    val dst = getMat()
    Imgproc.medianBlur(img, dst, radius)
    dst
  }
}
