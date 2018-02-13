package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.service.impl.{ImagePixelLocatorService, LocalFileManager}
import com.smartrural.estimator.service.{FileManagerService, PixelLocatorService}
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
class GaussianFilterTransformerTest extends FlatSpec with MockFactory{

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")

  implicit val inj = new Module{
    bind[FileManagerService] to new LocalFileManager
    bind[PixelLocatorService] to new ImagePixelLocatorService()
  }

  val filter = new GaussianFilterTransformer(6, 1, 1)

  behavior of "GaussianFilterTransformer"

  it should "Calculate the gaussian normalized values" in {
    val dstImage = "z-img-000-000004-gauss.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = ImageIO.read(image)
    ImageIO.write(filter.transform(originalImage), JpgFormat, dstFile)
    assert(dstFile.exists())
  }
}
