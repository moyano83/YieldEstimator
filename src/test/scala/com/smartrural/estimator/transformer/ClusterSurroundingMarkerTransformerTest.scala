package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.service.impl.LocalFileManager
import com.smartrural.estimator.util.AppConstants
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by jm186111 on 14/02/2018.
  */

@RunWith(classOf[JUnitRunner])
class ClusterSurroundingMarkerTransformerTest extends FlatSpec with MockFactory {

  val fileManager = new LocalFileManager

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "result_inferences/valdemonjas-2017-09-13_01/z-img-000-000004.png")

  val filter = new ClusterSurroundingMarkerTransformer(10)

  behavior of "ClusterSurroundingMarkerTransformer"

  it should "execute the filter transformation" in {
    println(filter.filterName)
    val dstImage = "z-img-000-000004-cluster-surrounding.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = fileManager.readImageAsMat(image)
    fileManager.writeImage(filter.applyTransform(originalImage), dstFile)
    assert(dstFile.exists())
  }
}