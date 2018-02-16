package com.smartrural.estimator.transformer

import java.io.File

import com.smartrural.estimator.service.impl.LocalFileManager
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class MedianFilterTransformerTest extends FlatSpec{

  val fileManager = new LocalFileManager

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")

  val filter = new MedianFilterTransformer(5)

  behavior of "MedianFilterTransformer"

  it should "execute the filter transformation" in {
    println(filter.filterName)
    val dstImage = "z-img-000-000004-median.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = fileManager.readImageAsMat(image)
    fileManager.writeImage(filter.applyTransform(originalImage), dstFile)
    assert(dstFile.exists())
  }
}
