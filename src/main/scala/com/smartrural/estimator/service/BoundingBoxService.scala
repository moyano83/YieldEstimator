package com.smartrural.estimator.service

import java.io.File

import com.smartrural.estimator.model.InferenceInfo

/**
  * Created by jm186111 on 31/01/2018.
  */
trait BoundingBoxService {
  /**
    * Reads the bboxFile provided
    * @param bboxFilePath the file to read
    * @return the Inferences extracted from the file
    */
  def readBBoxFile(bboxFilePath:File):Iterator[InferenceInfo]

  /**
    * Gets the specific inferences for the provided fileName
    * @param bboxFilePath  the file to read
    * @param imageName the image name to extract the inferences for
    * @return the Inferences extracted from the file for the specific filename
    */
  def getFilteredInferenceInfo(bboxFilePath:File, imageName:String):Iterator[InferenceInfo]
}
