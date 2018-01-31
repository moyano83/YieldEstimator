package com.smartrural.estimator.service

import java.io.File

import com.smartrural.estimator.model.PixelCoordinates

/**
  * Created by jm186111 on 31/01/2018.
  */
trait PixelLocatorService {
  def findSurroundingClusterPixels(image:File, radius:Int):Set[PixelCoordinates]
}
