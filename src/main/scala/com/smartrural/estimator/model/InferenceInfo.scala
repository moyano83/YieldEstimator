package com.smartrural.estimator.model

/**
  * Class that represent information about the inference
  * @param `type` the type of inference
  * @param geometry the InferenceGeometry
  * @param properties the InferenceProperties
  */
case class InferenceInfo(`type`:String, geometry:InferenceGeometry, properties:InferenceProperties)

/**
  * Class that holds the type of coordinates and its location
  * @param `type` the type of coordinates
  * @param coordinates Array containing the latitude and longitude of the Inference
  */
case class InferenceGeometry(`type`:String, coordinates:Array[Double])

/**
  * Class representing the list of properties that the
  * @param url url to the image in cartodb
  * @param width width of the original image
  * @param height height of the original image
  * @param pixels number of pixels identified as clusters
  * @param clusters numer of clusters
  */
case class InferenceProperties(url:String, width:Int, height:Int, pixels:String, clusters:String)