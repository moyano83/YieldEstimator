package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage

/**
  * Created by jm186111 on 12/02/2018.
  */
trait ImageTransformer {
  /**
    * Transform an imagen according to the internal implementation of the Transformer
    * @param img the image to transform
    * @return the transformed image
    */
  def transform(img:BufferedImage):BufferedImage

  /**
    * Gets a void image with the same size than the original one
    * @param img the image to base the copy to
    * @return the bufferedImage
    */
  def getImageCanvas(img:BufferedImage):BufferedImage =
    new BufferedImage(img.getWidth, img.getHeight, BufferedImage.TYPE_INT_RGB)
}
