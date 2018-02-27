package com.smartrural.estimator.model

/**
  * Created by jm186111 on 27/02/2018.
  */
case class InferenceInfo(`type`:String, geometry:InferenceGeometry, properties:InferenceProperties)

case class InferenceGeometry(`type`:String, coordinates:Array[Double])

case class InferenceProperties(cartodb_id:Int, url:String, width:Int, height:Int, pixels:Int, clusters:Int, cm2:Int)