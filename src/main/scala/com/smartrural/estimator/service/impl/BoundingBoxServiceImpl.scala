package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.model.BBoxItemInfo
import com.smartrural.estimator.service.BoundingBoxService
import com.smartrural.estimator.util.AppConstants

class BoundingBoxServiceImpl(percentegeFilter:Double = 0.85) extends BoundingBoxService{

  /**
    * @inheritdoc
    */
  override def readBBoxFile(bboxFile:File):Map[String, List[BBoxItemInfo]] =
    scala.io.Source.fromFile(bboxFile).getLines()
      .map(getInferenceInfoFromLine)
      .filter(_.matchProbability >= percentegeFilter)
      .toList
      .groupBy(_.imageName)

  /**
    * Parses the string line into a BBoxItemInfo class
    * @param line the string line to parse
    * @return the BBoxItemInfo
    */
  private def getInferenceInfoFromLine(line:String):BBoxItemInfo ={
    val lineItems = line.split(" ")
    BBoxItemInfo(lineItems(0),
    lineItems(1),
    lineItems(2).toDouble,
    lineItems(3).split("\\.").head.toInt,
    lineItems(4).split("\\.").head.toInt,
    lineItems(5).split("\\.").head.toInt,
    lineItems(6).split("\\.").head.toInt)
  }
}