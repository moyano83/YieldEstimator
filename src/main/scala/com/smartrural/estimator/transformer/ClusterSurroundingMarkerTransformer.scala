package com.smartrural.estimator.transformer

import com.smartrural.estimator.util.ImageUtils._
import com.smartrural.estimator.util.{AppConstants, ImageUtils}
import org.opencv.core.Mat

/**
  * Created by jm186111 on 14/02/2018.
  */
class ClusterSurroundingMarkerTransformer (radius:Int = 5) extends ImageTransformer {

  /**
    * Transform an imagen according to the internal implementation of the Transformer
    *
    * @param imageMat the image to transform
    * @return the transformed image
    */
  override def applyTransform(imageMat: Mat): Mat = {
    val dst = getMat(imageMat.rows(), imageMat.cols())
    ImageUtils.getMatAsColoredPixels(imageMat)
      .map(pixel=> extractSurroundingPixels(imageMat, radius, pixel))
      .flatten
      .toSet
      .filter(_.isVoid()).foreach(pixel => dst.put(pixel.row, pixel.col, AppConstants.RedColor))
    dst
  }
}
