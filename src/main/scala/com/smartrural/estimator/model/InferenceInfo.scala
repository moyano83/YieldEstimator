package com.smartrural.estimator.model

case class InferenceInfo(imageName:String,
                         matchType:String,
                         matchProbability:Double,
                         xMin:Int,
                         xMax:Int,
                         yMin:Int,
                         yMax:Int)
