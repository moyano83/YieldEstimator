package com.smartrural.estimator.model

/**
  * Model class for the BBox info file content
  * @param imageName the image name
  * @param matchType type of the match, this attribute is always "cluster"
  * @param matchProbability the probability of the detected match
  * @param colMin column min value
  * @param rowMin row min value
  * @param colMax column max value
  * @param rowMax row max value
  */
case class BBoxItemInfo(imageName:String,
                        matchType:String,
                        matchProbability:Double,
                        colMin:Int,
                        rowMin:Int,
                        colMax:Int,
                        rowMax:Int){
  /**
    * The width of the image
    */
  val ImageWidth:Int = colMax - colMin
  /**
    * The height of the image
    */
  val ImageHeight:Int = rowMax - rowMin

  /**
    * Method that returns the relative position of the column pass into the image this inference is located
    * @param col the column value
    * @return the relative column value
    */
  def getColAdjusted(col:Int):Int = colMin + col
  /**
    * Method that returns the relative position of the row pass into the image this inference is located
    * @param row the row value
    * @return the relative row value
    */
  def getRowAdjusted(row:Int):Int = rowMin + row
}
