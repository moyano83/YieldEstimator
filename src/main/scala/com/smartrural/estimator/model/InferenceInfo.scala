package com.smartrural.estimator.model

case class InferenceInfo(imageName:String,
                         matchType:String,
                         matchProbability:Double,
                         xMin:Int,
                         yMin:Int,
                         xMax:Int,
                         yMax:Int){

  val YMaxRange:Int = yMax - yMin

  val XMaxRange:Int = xMax - xMin

  def getYAdjusted(y:Int):Int = yMin + y

  def getXAdjusted(x:Int):Int = xMin + x

  def getResolution: String = s"$XMaxRange x $YMaxRange"
}
