package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.service.impl.LocalFileManager
import com.smartrural.estimator.util.AppConstants.PngFormat
import com.smartrural.estimator.util.ImageUtils
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class HistogramFilterTransformerTest extends FlatSpec{

  val fileManager = new LocalFileManager

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val imageSampleBuffer = ImageIO.read(new File(rootPathFile, "sample.jpg"))
  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")

  val filter = new HistogramFilterTransformer(ImageUtils.toMat(imageSampleBuffer))

  behavior of "HistogramFilterTransformer"

  it should "execute the filter transformation" in {
    println(filter.filterName)
    val dstImage = "z-img-000-000004-histogram.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = fileManager.readImageAsMat(image)
    fileManager.writeImage(filter.applyTransform(originalImage), dstFile)
    assert(dstFile.exists())
  }
}
