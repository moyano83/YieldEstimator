package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.util.AppConstants.JpgFormat
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class AvgBlurFilterTransformerTest extends FlatSpec with MockFactory{

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")

  val filter = new AvgBlurFilterTransformer(2)

  behavior of "AvgBlurFilterTransformer"

  it should "Calculate the gaussian normalized values" in {
    println(filter.filterName)
    val dstImage = "z-img-000-000004-avg.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = ImageIO.read(image)
    ImageIO.write(filter.transform(originalImage), JpgFormat, dstFile)
    assert(dstFile.exists())
  }
}
