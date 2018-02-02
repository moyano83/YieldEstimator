package com.smartrural.estimator.service.impl

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PixelLocatorServiceTest extends FlatSpec{

  val image = new File(getClass.getClassLoader.getResource("./image.png").toURI.getPath)
  val pixelLocatorService = new ImagePixelLocatorService()
  behavior of "PixelLocatorService"

  it should "locate the non grape pixels surrounding the cluster with different radius" in {
    val pixels1 = pixelLocatorService.findSurroundingClusterPixels(image, 1)
    assert(pixels1.size == 28)
    val pixels2 = pixelLocatorService.findSurroundingClusterPixels(image, 2)
    assert(pixels2.size == 64)
    val pixels3 = pixelLocatorService.findSurroundingClusterPixels(image, 5)
    assert(pixels3.size == 64)
  }

}
