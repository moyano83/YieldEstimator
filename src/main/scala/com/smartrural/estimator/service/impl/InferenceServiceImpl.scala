package com.smartrural.estimator.service.impl

import java.io.File
import java.nio.charset.StandardCharsets

import com.smartrural.estimator.model.InferenceInfo
import com.smartrural.estimator.service.InferenceService
import org.apache.commons.io.FileUtils
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.collection.JavaConversions._
/**
  * Created by jm186111 on 27/02/2018.
  */
class InferenceServiceImpl extends InferenceService{

  /**
    * list of inference info to be read only once
    */
  private[this] var inferences: Option[List[InferenceInfo]] = None
  /**
    * The implicit to write the file
    */
  implicit val formats = DefaultFormats

  /**
    * @inheritdoc
    */
  override def readInferencesFile(inferencesFilePath:File):List[InferenceInfo] ={
    if (!inferences.isDefined){
      inferences = Some(FileUtils.readLines(inferencesFilePath, StandardCharsets.UTF_8).map(parseInfo).toList)
    }
    inferences.get
  }

  /**
    * parses the string line into an object of type InferenceInfo
    * @param line the line to parse
    * @return the inference info
    */
  private def parseInfo(line:String):InferenceInfo = parse(line).extract[InferenceInfo]
}
