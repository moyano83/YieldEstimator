package com.smartrural.estimator.util

import java.io.File

import org.junit.runner.RunWith
import org.opencv.core.Core
import org.opencv.imgcodecs.Imgcodecs
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 27/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class ImageUtilsTest extends FlatSpec{
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  import ImageUtils._

  behavior of "ImageUtils"

  it should "get the formatted resolution" in {
    assert(getFormattedResolution(13, 12) == "13 x 12")
  }

  it should "find the surrounding non void pixels" in {
    val img = Imgcodecs.imread(new File(rootPathFile, "Image.png").getAbsolutePath)
    val pixels = extractAllSurroundingVoidPixelsFromImage(img, 2)
    assert(pixels.size == 64)
  }
}
