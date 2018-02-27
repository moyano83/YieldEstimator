package com.smartrural.estimator.model

/**
  * Created by jm186111 on 26/02/2018.
  */
case class VineYieldParameters(pictureName:String,
                            cartoDBId:Int,
                            latitude:Double,
                            longitude:Double,
                            pixels:Int,
                            cm2:Int,
                            occlusionPercentage:Double){

  def this(inference:InferenceInfo, percentage:Double) = this(
    inference.properties.url,
    inference.properties.cartodb_id,
    inference.geometry.coordinates(0),
    inference.geometry.coordinates(1),
    inference.properties.pixels,
    inference.properties.cm2,
    percentage
  )
  override def toString: String = Array(pictureName,
    cartoDBId.toString,
    latitude.toString,
    longitude.toString,
    pixels.toString,
    cm2.toString,
    occlusionPercentage.toString).mkString(",")
}
