package com.smartrural.estimator.transformer.impl

import com.smartrural.estimator.transformer.ImageTransformer
import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core.{Core, Mat, Scalar}

/**
  * Hue filter transformer, filter out the pixels that contains hue, saturation or values outside the range
  * @param hueColor hue color range
  * @param saturation saturation color range
  * @param value value color range
  */
class HueFilterImageTransformer(hueColor:Range, saturation:Range, value:Range) extends ImageTransformer {
  /**
    * @inheritdoc
    */
  override def transform(imgMat:Mat): Mat = {
    val lowFilter = new Scalar(hueColor.min, saturation.min, value.min)
    val highFilter = new Scalar(hueColor.max, saturation.max, value.max) // upper range is not inclusive
    val hsvImage = getCVT(imgMat)

    val dst, mask = getMat()
    hsvImage.copyTo(mask)
    Core.inRange(hsvImage, lowFilter, highFilter, mask)

    hsvImage.copyTo(dst, mask)
    getRGB(dst)
  }

}
