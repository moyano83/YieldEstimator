package com.smartrural.estimator.transformer

import org.opencv.core.Mat
import org.slf4j.LoggerFactory


/**
  * Created by jm186111 on 12/02/2018.
  */
trait ImageTransformer {
  /**
    * Filter Class name
    */
  val filterName = getClass.getSimpleName
  /**
    * Class logger
    */
  val logger = LoggerFactory.getLogger(getClass)
  /**
    * Transform an imagen according to the internal implementation of the Transformer
    * @param img the image to transform
    * @return the transformed image
    */
  def applyTransform(img:Mat):Mat
}
