package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.model.InferenceInfo
import com.smartrural.estimator.service.BoundingBoxService
import com.smartrural.estimator.util.AppConstants

class BoundingBoxTextReaderService extends BoundingBoxService{

  override def readBBoxFile(bboxFile:File):Map[String, List[InferenceInfo]] =
    scala.io.Source.fromFile(bboxFile).getLines().map(getInferenceInfoFromLine).toList.groupBy(_.imageName)

  private def getInferenceInfoFromLine(line:String):InferenceInfo ={
    val lineItems = line.split(" ")
    InferenceInfo(lineItems(0),
    lineItems(1),
    lineItems(2).toDouble,
    lineItems(3).split("\\.").head.toInt,
    lineItems(4).split("\\.").head.toInt,
    lineItems(5).split("\\.").head.toInt,
    lineItems(6).split("\\.").head.toInt)
  }
}