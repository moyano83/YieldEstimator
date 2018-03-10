package com.smartrural.estimator.transformer

import java.io.File

import com.smartrural.estimator.service.impl.FileManagerServiceImpl
import com.smartrural.estimator.transformer.impl.AvgBlurFilterTransformer
import org.junit.runner.RunWith
import org.opencv.core.Core
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class AvgBlurFilterTransformerTest extends FlatSpec with MockFactory{

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val fileManager = new FileManagerServiceImpl

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "FilteredImage2.png")

  val filter = new AvgBlurFilterTransformer(4)

  behavior of "AvgBlurFilterTransformer"

  it should "execute the filter transformation" in {
    println(filter.filterName)
    val dstImage = "FilteredImage2-avg.png"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = fileManager.readImage(image)
    fileManager.writeImage(filter.transform(image, originalImage), dstFile)
    assert(dstFile.exists())
  }
}
