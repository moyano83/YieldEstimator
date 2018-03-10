package com.smartrural.estimator.util

import java.io.File

import com.smartrural.estimator.model.BinaryPixel
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

  it should "find the surrounding non void pixels with the pixels to inspect being a subset of the total" in {
    val allPixels = List(
      BinaryPixel(false, 1, 1)
    )
    val surrounding =
      extractAllSurroundingVoidPixelsFromImage(allPixels, BinaryPixel(false, 2, 2) :: allPixels, 5, 5, 1)
    assert(surrounding.size == 7)
    assert(surrounding.contains(BinaryPixel(true, 0, 0)))
    assert(surrounding.contains(BinaryPixel(true, 0, 1)))
    assert(surrounding.contains(BinaryPixel(true, 0, 2)))
    assert(surrounding.contains(BinaryPixel(true, 1, 0)))
    assert(surrounding.contains(BinaryPixel(true, 1, 2)))
    assert(surrounding.contains(BinaryPixel(true, 2, 0)))
    assert(surrounding.contains(BinaryPixel(true, 2, 1)))
  }

  it should "find the surrounding non void pixels" in {
    val allPixels = List(
      BinaryPixel(false, 1, 1),
      BinaryPixel(false, 3, 3)
    )
    val surrounding = extractAllSurroundingVoidPixelsFromImage(allPixels,allPixels, 5, 5, 1)
    assert(surrounding.size == 15)
    assert(surrounding.contains(BinaryPixel(true, 0, 0)))
    assert(surrounding.contains(BinaryPixel(true, 0, 1)))
    assert(surrounding.contains(BinaryPixel(true, 0, 2)))
    assert(surrounding.contains(BinaryPixel(true, 1, 0)))
    assert(surrounding.contains(BinaryPixel(true, 1, 2)))
    assert(surrounding.contains(BinaryPixel(true, 2, 0)))
    assert(surrounding.contains(BinaryPixel(true, 2, 1)))
    assert(surrounding.contains(BinaryPixel(true, 2, 2)))
    assert(surrounding.contains(BinaryPixel(true, 2, 3)))
    assert(surrounding.contains(BinaryPixel(true, 2, 4)))
    assert(surrounding.contains(BinaryPixel(true, 3, 2)))
    assert(surrounding.contains(BinaryPixel(true, 3, 4)))
    assert(surrounding.contains(BinaryPixel(true, 4, 2)))
    assert(surrounding.contains(BinaryPixel(true, 4, 3)))
    assert(surrounding.contains(BinaryPixel(true, 4, 4)))
  }

  it should "find the surrounding non void pixels on a bigger list" in {
    val allPixels = List(
      BinaryPixel(false, 1, 1),
      BinaryPixel(false, 3, 3)
    )
    val surrounding = extractAllSurroundingVoidPixelsFromImage(allPixels, allPixels, 6, 6, 2)
    assert(surrounding.size == 30)
    assert(surrounding.contains(BinaryPixel(true, 0, 0)))
    assert(surrounding.contains(BinaryPixel(true, 0, 1)))
    assert(surrounding.contains(BinaryPixel(true, 0, 2)))
    assert(surrounding.contains(BinaryPixel(true, 0, 3)))
    assert(surrounding.contains(BinaryPixel(true, 1, 0)))
    assert(surrounding.contains(BinaryPixel(true, 1, 2)))
    assert(surrounding.contains(BinaryPixel(true, 1, 3)))
    assert(surrounding.contains(BinaryPixel(true, 1, 4)))
    assert(surrounding.contains(BinaryPixel(true, 1, 5)))
    assert(surrounding.contains(BinaryPixel(true, 2, 0)))
    assert(surrounding.contains(BinaryPixel(true, 2, 1)))
    assert(surrounding.contains(BinaryPixel(true, 2, 2)))
    assert(surrounding.contains(BinaryPixel(true, 2, 3)))
    assert(surrounding.contains(BinaryPixel(true, 2, 4)))
    assert(surrounding.contains(BinaryPixel(true, 2, 5)))
    assert(surrounding.contains(BinaryPixel(true, 3, 0)))
    assert(surrounding.contains(BinaryPixel(true, 3, 1)))
    assert(surrounding.contains(BinaryPixel(true, 3, 2)))
    assert(surrounding.contains(BinaryPixel(true, 3, 4)))
    assert(surrounding.contains(BinaryPixel(true, 3, 5)))
    assert(surrounding.contains(BinaryPixel(true, 4, 1)))
    assert(surrounding.contains(BinaryPixel(true, 4, 2)))
    assert(surrounding.contains(BinaryPixel(true, 4, 3)))
    assert(surrounding.contains(BinaryPixel(true, 4, 4)))
    assert(surrounding.contains(BinaryPixel(true, 4, 5)))
    assert(surrounding.contains(BinaryPixel(true, 5, 1)))
    assert(surrounding.contains(BinaryPixel(true, 5, 2)))
    assert(surrounding.contains(BinaryPixel(true, 5, 3)))
    assert(surrounding.contains(BinaryPixel(true, 5, 4)))
    assert(surrounding.contains(BinaryPixel(true, 5, 5)))
  }
}
