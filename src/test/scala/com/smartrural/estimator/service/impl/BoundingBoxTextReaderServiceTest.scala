package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.util.AppConstants
import org.junit.runner.RunWith
import org.opencv.core.Core
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoundingBoxTextReaderServiceTest extends FlatSpec{

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val bboxFilePath = s"bbox_info/valdemonjas-2017-09-13_01/${AppConstants.BbBoxesFileName}"

  behavior of "BoundingBoxTextReader"

  val bbFile = new File(getClass.getClassLoader.getResource(s"${bboxFilePath}").getPath)
  val bboxService = new BoundingBoxTextReaderService
  it should "read the text bounding box file" in {
    val mapOfInferences = bboxService.readBBoxFile(bbFile)
    assert(mapOfInferences.size == 2)
    assert(mapOfInferences.getOrElse("z-img-000-000004.jpg", Nil).size == 7)
    assert(mapOfInferences.getOrElse("z-img-000-000005.jpg", Nil).size == 2)
  }
}
