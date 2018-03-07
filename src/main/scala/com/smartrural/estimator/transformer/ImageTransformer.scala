package com.smartrural.estimator.transformer

import org.opencv.core.Mat
import org.slf4j.LoggerFactory


/**
  * Image transformer
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
  def transform(img:Mat):Mat
}
