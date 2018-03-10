package com.smartrural.estimator.transformer

import java.io.File

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
    * @param matFile File representing the image
    * @param img the image to transform
    * @return the transformed image
    */
  def transform(matFile:File, img:Mat):Mat
}
