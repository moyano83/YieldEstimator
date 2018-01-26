package com.smartrural.estimator.service

import javax.imageio.ImageIO

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PixelLocatorServiceTest extends FlatSpec{

  val image = ImageIO.read(getClass.getClassLoader.getResourceAsStream("./image.png"))
  val pixelLocatorService = PixelLocatorService(image)
  behavior of "PixelLocatorService"

  it should "locate the non grape pixels surrounding the cluster with different radius" in {
    val pixels1 = pixelLocatorService.findSurroundingClusterPixels(1)
    assert(pixels1.size == 28)
    val pixels2 = pixelLocatorService.findSurroundingClusterPixels(2)
    assert(pixels2.size == 64)
    val pixels3 = pixelLocatorService.findSurroundingClusterPixels(5)
    assert(pixels3.size == 64)
  }

}
