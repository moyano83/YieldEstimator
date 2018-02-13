package com.smartrural.estimator.transformer

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.service.PixelLocatorService
import com.smartrural.estimator.util.AppConstants.JpgFormat
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class MedianFilterTransformerTest extends FlatSpec with MockFactory{

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")

  implicit val inj = new Module{
    bind[PixelLocatorService] to new PixelLocatorService() {
      override def findSurroundingClusterPixels(image: BufferedImage, radius: Int): Set[ColoredPixel] = Set()
    }
  }

  val filter = new MedianFilterTransformer(3)

  behavior of "MedianFilterTransformer"

  it should "Calculate the gaussian normalized values" in {
    val dstImage = "z-img-000-000004-median.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = ImageIO.read(image)
    ImageIO.write(filter.transform(originalImage), JpgFormat, dstFile)
    assert(dstFile.exists())
  }
}
