package com.smartrural.estimator.service

import com.smartrural.estimator.model.PixelCoordinates

/**
  * Created by jm186111 on 31/01/2018.
  */
trait PixelLocatorService {
  def findSurroundingClusterPixels(radius:Int):Set[PixelCoordinates]
}
