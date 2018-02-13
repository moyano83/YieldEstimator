package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.service.FileManagerService
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module


@RunWith(classOf[JUnitRunner])
class PixelLocatorServiceTest extends FlatSpec{

  val fileManagerService = new LocalFileManager
  val pixelLocatorService = new ImagePixelLocatorService()(new Module{
    bind[FileManagerService] to fileManagerService
  })
  val image = fileManagerService.readImage(new File(getClass.getClassLoader.getResource("./image.png").toURI.getPath))

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
