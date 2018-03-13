package com.smartrural.estimator.service.impl

import java.io.File

import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 27/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class InferenceServiceImplTest extends FlatSpec with MockFactory{

  val inferenceService = new InferenceServiceImpl

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val inferencesFile = new File(rootPathFile, s"inferences_info/valdemonjas_2017_09_13_inference.geojson")

  val testImageUrl = "https://s3.amazonaws.com/tileo-datasets/quadvine/2017/valdemonjas/valdemonjas-2017-09-13-inferences-2/valdemonjas-2017-09-13_13/z-img-000-000032.jpg"

  behavior of "LocalInferenceService"

  it should "parse the given file" in {
    val listInferences = inferenceService.readInferencesFile(inferencesFile)
    assert(listInferences.size == 3)
    assert(listInferences.find(_.properties.url == testImageUrl).isDefined)
  }

  it should "return the image with the specified name" in {
    val inference = inferenceService.getInferenceByPictureName(inferencesFile, "valdemonjas-2017-09-13_13/z-img-000-000032.jpg")
    assert(inference.isDefined)
    assert(inference.get.properties.url == testImageUrl)
  }

}
