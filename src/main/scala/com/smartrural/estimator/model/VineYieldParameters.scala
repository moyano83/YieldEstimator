package com.smartrural.estimator.model

import com.smartrural.estimator.util.AppConstants

/**
  * Created by jm186111 on 26/02/2018.
  */
case class VineYieldParameters(pictureName:String,
                            latitude:Double,
                            longitude:Double,
                            pixels:Int,
                            clusters:Int,
                            occlusionPercentage:Double){
  /**
    * Secondary constructor
    * @param inference
    * @param percentage
    * @return
    */
  def this(inference:InferenceInfo, percentage:Double) = this(
    inference.properties.url,
    inference.geometry.coordinates(0),
    inference.geometry.coordinates(1),
    inference.properties.pixels.toInt,
    inference.properties.clusters.toInt,
    percentage
  )
  /**
    * @inheritdoc
    */
  override def toString: String = Array(pictureName,
    latitude.toString,
    longitude.toString,
    clusters.toString,
    pixels.toString,
    AppConstants.NumberFormatter.format(occlusionPercentage)).mkString(",")
}
