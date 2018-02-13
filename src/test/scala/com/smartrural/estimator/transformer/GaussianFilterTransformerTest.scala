package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.service.PixelLocatorService
import com.smartrural.estimator.util.AppConstants
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

  val gaussFilter = new GaussianFilterTransformer(radius = 3)

  behavior of "AvgBlurFilterTransformer"

  it should "Calculate the gaussian normalized values" in {
    val originalImage = ImageIO.read(image)
    ImageIO.write(gaussFilter.transform(originalImage), AppConstants.JpgFormat, new File(rootPathFile,
      "z-img-000-000004-gauss.jpg"))
  }
}
