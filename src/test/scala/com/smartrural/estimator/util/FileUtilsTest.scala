package com.smartrural.estimator.util

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class FileUtilsTest extends FlatSpec{

  val inferencesPath = getClass.getClassLoader.getResource("inferences").getPath

  behavior of "FileUtils"

  it should "List all the child files" in {
    val inferencesFolder = FileUtils.getChildList(inferencesPath)
    assert(inferencesFolder.size == 1)
    assert(inferencesFolder.head.getName == "valdemonjas-2017-09-13_01")

    val picturesList = FileUtils.getChildList(inferencesFolder.head.getPath)
    assert(picturesList.size == 7)
  }
}
