package com.smartrural.estimator.service

import java.io.File

import com.smartrural.estimator.model.InferenceInfo

/**
  * Created by jm186111 on 31/01/2018.
  */
trait BoundingBoxService {

  def readBBoxFile(bboxFilePath:File):Iterator[InferenceInfo]

  def getFilteredInferenceInfo(bboxFilePath:File, imageName:String):Iterator[InferenceInfo]
}
