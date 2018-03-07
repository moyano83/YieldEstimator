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
    * @param colOffset the column offset value
    * @return the relative column value
    */
  def getColWithOffset(colOffset:Int):Int = colMin + colOffset
  /**
    * Method that returns the relative position of the row pass into the image this inference is located
    * @param rowOffset the row offset value
    * @return the relative row value
    */
  def getRowWithOffset(rowOffset:Int):Int = rowMin + rowOffset
}
