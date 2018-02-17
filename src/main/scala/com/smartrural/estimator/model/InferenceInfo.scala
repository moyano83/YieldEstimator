package com.smartrural.estimator.model

import com.smartrural.estimator.util.ImageUtils

case class InferenceInfo(imageName:String,
                         matchType:String,
                         matchProbability:Double,
                         colMin:Int,
                         rowMin:Int,
                         colMax:Int,
                         rowMax:Int){

  val ColMaxRange:Int = colMax - colMin

  val RowMaxRange:Int = rowMax - rowMin

  def getColAdjusted(col:Int):Int = colMin + col

  def getRowAdjusted(row:Int):Int = rowMin + row

  def getResolution(): String = ImageUtils.getFormattedResolution(RowMaxRange, ColMaxRange)
}
