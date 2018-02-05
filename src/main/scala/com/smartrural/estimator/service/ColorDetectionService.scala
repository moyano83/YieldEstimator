package com.smartrural.estimator.service

import com.smartrural.estimator.model.RGBPixel

/**
  * Created by jm186111 on 05/02/2018.
  */
trait ColorDetectionService {
  /**
    * Returns a Boolean indicating if the pixel is within the configured color value
    * @param pixel the rgb value
    * @return true if it is inside the range
    */
  def isWithinRange(pixel: RGBPixel):Boolean
}
