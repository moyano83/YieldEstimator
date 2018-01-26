package com.smartrural.estimator.file

import java.io.File

import com.smartrural.estimator.model.InferenceInfo

object BoundingBoxTextReader {

  def readBBoxFile(bboxFile:File):Iterator[InferenceInfo] =
      scala.io.Source.fromFile(bboxFile).getLines().map(getInferenceInfoFromLine)

  def getInferenceInfoFromLine(line:String):InferenceInfo ={
    val lineItems = line.split(" ")
    InferenceInfo(lineItems(0),
      lineItems(1),
      lineItems(2).toDouble,
      lineItems(3).split("\\.").head.toInt,
      lineItems(4).split("\\.").head.toInt,
      lineItems(5).split("\\.").head.toInt,
      lineItems(6).split("\\.").head.toInt)
  }

  def getFilteredInferenceInfo(bboxFile:File, fileName:String) = readBBoxFile(bboxFile).filter(_.imageName == fileName)

}
