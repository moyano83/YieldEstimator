package com.smartrural.estimator.file

import java.io.File

import com.smartrural.estimator.service.impl.BoundingBoxTextReaderService
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoundingBoxTextReaderServiceTest extends FlatSpec{


  behavior of "BoundingBoxTextReader"

  val bbFile = new File(getClass.getClassLoader.getResource("inferences_info/valdemonjas-2017-09-13_01").getPath)
  val bboxService = new BoundingBoxTextReaderService
  it should "read the text bounding box file" in {
    assert(bboxService.readBBoxFile(bbFile).size == 9)
  }

  it should "get The info related with the filename" in {
    assert(bboxService.getFilteredInferenceInfo(bbFile, "z-img-000-000005.jpg").size == 2)
  }
}
