package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.model.InferenceInfo
import com.smartrural.estimator.service.BoundingBoxService
import com.smartrural.estimator.util.AppConstants

class BoundingBoxTextReaderService extends BoundingBoxService{

  override def readBBoxFile(bboxFilePath:File):Iterator[InferenceInfo] =
    scala.io.Source.fromFile(new File(bboxFilePath, AppConstants.BbBoxesFileName))
      .getLines().map(getInferenceInfoFromLine)

  override def getFilteredInferenceInfo(bboxFilePath:File, imageName:String) =
    readBBoxFile(bboxFilePath).filter(_.imageName == imageName)

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