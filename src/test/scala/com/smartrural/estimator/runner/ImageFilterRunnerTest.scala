package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.impl.FileManagerServiceImpl
import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService}
import com.smartrural.estimator.transformer._
import com.smartrural.estimator.transformer.impl.{GaussianFilterTransformer, HistogramFilterTransformer, MedianFilterTransformer}
import org.junit.runner.RunWith
import org.opencv.core.Core
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module

/**
  * Created by jm186111 on 01/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class ImageFilterRunnerTest extends FlatSpec with MockFactory{

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val fileManager = new FileManagerServiceImpl

  val boundingBoxService = mock[BoundingBoxService]
  (boundingBoxService.getDistinctImages _).expects(*).onCall{(x:File) => (x.getName, Set("z-img-000-000004.jpg"))}.once

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val imageSample = fileManager.readImage(new File(rootPathFile, "sample.jpg"))

  val destinationFolder = new File(rootPathFile, "imgRunner")

  val radius = 5

  val filterList = List(
    new MedianFilterTransformer(radius),
    new GaussianFilterTransformer(radius),
    new HistogramFilterTransformer(radius, imageSample)
  )
  implicit val inj = new Module{
    bind[FileManagerService] to fileManager
    bind[BoundingBoxService] to boundingBoxService
  }
  behavior of "ImageFilterRunner"

  val runner = new ImageFilterRunner(
    new File(rootPathFile, "inferences.json"),
    new File(rootPathFile, "mask").getAbsolutePath,
    new File(rootPathFile, "original_images").getAbsolutePath,
    destinationFolder.getAbsolutePath,
    filterList
  )

  it should "generate a result image by applying all filters" in {
    if(destinationFolder.exists()) destinationFolder.delete()
    runner.run()
    assert(true)
  }
}
