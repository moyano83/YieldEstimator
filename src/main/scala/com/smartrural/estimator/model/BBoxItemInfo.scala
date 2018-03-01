package com.smartrural.estimator.model

case class BBoxItemInfo(imageName:String,
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
}
