package com.smartrural.estimator.transformer

import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core.{Core, Mat, Scalar}

/**
  * Created by jm186111 on 05/02/2018.
  */
class HueFilterImageTransformer(hueColor:Range, saturation:Range, value:Range) extends ImageTransformer {

  override def applyTransform(imgMat:Mat): Mat = {
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
