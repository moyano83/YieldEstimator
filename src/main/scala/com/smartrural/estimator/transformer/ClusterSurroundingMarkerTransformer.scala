package com.smartrural.estimator.transformer

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.AppConstants
import com.smartrural.estimator.util.ImageUtils._
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
    val image = toBufferedImage(imageMat, None)
    val dstImage = getImageCanvas(image, false)
    val pixelsToCopy = (for {
        x <- 0 until image.getWidth;
        y <- 0 until image.getHeight;
        pixel = new ColoredPixel(image, x, y) if !pixel.isVoid
      } yield extractSurroundingPixels(image, radius, pixel)).flatten.toSet

    pixelsToCopy.filter(_.isVoid()).foreach(pixel =>{
      dstImage.setRGB(pixel.x, pixel.y, AppConstants.RedColor)
    })

    toMat(dstImage)
  }
}
