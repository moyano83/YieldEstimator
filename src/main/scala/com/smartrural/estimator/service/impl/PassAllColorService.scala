package com.smartrural.estimator.service.impl

import com.smartrural.estimator.model.RGBPixel
import com.smartrural.estimator.service.ColorDetectionService

/**
  * Created by jm186111 on 10/02/2018.
  */
class PassAllColorService extends ColorDetectionService{
  /**
    * Returns a Boolean indicating if the pixel is within the configured color value
    *
    * @param pixel the rgb value
    * @return true if it is inside the range
    */
  override def isWithinRange(pixel: RGBPixel): Boolean = true
}
