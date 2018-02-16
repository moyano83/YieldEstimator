package com.smartrural.estimator.transformer

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.AppConstants
import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core.{Core, CvType, Mat}

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
    val dst = new Mat(imageMat.width(), imageMat.height(), CvType.CV_8UC3)
    val pixelsToCopy = (for {
        x <- 0 until imageMat.width();
        y <- 0 until imageMat.height;
        pixel = arrayToColoredPixel(imageMat, x, y) if !pixel.isVoid
      } yield extractSurroundingPixels(imageMat, radius, pixel)).flatten.toSet

    pixelsToCopy.filter(_.isVoid()).foreach(pixel =>{
      dst.put(pixel.x, pixel.y, AppConstants.RedColor)
    })

    dst
  }
}
