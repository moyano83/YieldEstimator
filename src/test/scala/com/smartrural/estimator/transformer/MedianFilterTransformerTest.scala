package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.util.AppConstants.JpgFormat
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class MedianFilterTransformerTest extends FlatSpec{

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")

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
