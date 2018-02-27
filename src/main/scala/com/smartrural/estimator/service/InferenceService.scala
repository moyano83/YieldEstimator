package com.smartrural.estimator.service

import java.io.File

import com.smartrural.estimator.model.InferenceInfo

/**
  * Created by jm186111 on 31/01/2018.
  */
trait InferenceService {
  /**
    * Reads the inferences file provided
    * @param inferencesFilePath the file to read
    * @return the Inferences extracted from the file
    */
  def readInferencesFile(inferencesFilePath:File):List[InferenceInfo]

  /**
    * Gets the inference corresponding to the picture name passed
    * @param inferencesFilePath the file to read
    * @param name the picture name
    * @return the option containing the inference
    */
  def getInferenceByPictureName(inferencesFilePath:File, name:String):Option[InferenceInfo] =
    readInferencesFile(inferencesFilePath).find(inference => inference.properties.url.endsWith(name))
}
