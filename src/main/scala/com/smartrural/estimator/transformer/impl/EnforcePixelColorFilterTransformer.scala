package com.smartrural.estimator.transformer.impl

import java.io.File

import com.smartrural.estimator.transformer.ImageTransformer
import com.smartrural.estimator.util.ImageUtils
import org.opencv.core.{Core, Mat, Size}
import org.opencv.imgproc.Imgproc

/**
  * Created by jm186111 on 10/03/2018.
  */
class EnforcePixelColorFilterTransformer extends ImageTransformer{
  /**
    * Transform an imagen according to the internal implementation of the Transformer
    *
    * @param matFile File representing the image
    * @param img     the image to transform
    * @return the transformed image
    */
  override def transform(matFile: File, img: Mat): Mat = {
    ImageUtils.getCopyMat(img)
  }
}
