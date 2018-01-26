package com.smartrural.estimator.file

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoundingBoxTextReaderTest extends FlatSpec{

  behavior of "BoundingBoxTextReader"

  val bbFile = new File(getClass.getClassLoader.getResource("inferences_info/valdemonjas-2017-09-13_01/bboxes.txt").getPath)

  it should "read the text bounding box file" in {
    assert(BoundingBoxTextReader.readBBoxFile(bbFile).size == 9)
  }

  it should "get The info related with the filename" in {
    assert(BoundingBoxTextReader.getFilteredInferenceInfo(bbFile, "z-img-000-000005.jpg").size == 2)
  }
}
