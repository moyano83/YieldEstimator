package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.service.impl.FileManagerServiceImpl
import com.smartrural.estimator.transformer.impl.HistogramFilterTransformer
import com.smartrural.estimator.util.ImageUtils
import org.junit.runner.RunWith
import org.opencv.core.Core
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class HistogramFilterTransformerTest extends FlatSpec{

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val fileManager = new FileManagerServiceImpl

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val imageSampleBuffer = fileManager.readImage(new File(rootPathFile, "sample.png"))
  //val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")
  val image = new File(rootPathFile, "z-img-000-000004-gauss.jpg")
  val filter = new HistogramFilterTransformer(5, imageSampleBuffer)

  behavior of "HistogramFilterTransformer"

  it should "execute the filter transformation" in {
    val dstImage = "z-img-000-000004-histogram.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = fileManager.readImage(image)
    fileManager.writeImage(filter.transform(originalImage), dstFile)
    assert(dstFile.exists())
  }
}
