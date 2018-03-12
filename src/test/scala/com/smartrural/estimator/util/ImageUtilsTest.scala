package com.smartrural.estimator.util

import java.io.File

import org.junit.runner.RunWith
import org.opencv.core.Core
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 27/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class ImageUtilsTest extends FlatSpec{
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  behavior of "ImageUtils"


}
