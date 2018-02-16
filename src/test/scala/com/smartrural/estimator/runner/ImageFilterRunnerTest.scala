package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService}
import com.smartrural.estimator.service.impl.{BoundingBoxTextReaderService, LocalFileManager}
import com.smartrural.estimator.transformer._
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module

/**
  * Created by jm186111 on 01/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class ImageFilterRunnerTest extends FlatSpec with MockFactory{

  val fileManager = new LocalFileManager

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val imageSampleBuffer = fileManager.readImageAsMat(new File(rootPathFile, "sample.jpg"))
  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")
  val bboxFilePath = s"inferences_info"

  val filterList = List(
    new MedianFilterTransformer(10),
    new GaussianFilterTransformer(10,1),
    new HistogramFilterTransformer(imageSampleBuffer)
  )
  implicit val inj = new Module{
    bind[FileManagerService] to fileManager
    bind[BoundingBoxService] to new BoundingBoxTextReaderService
  }
  behavior of "ImageFilterRunner"

  val runner = new ImageFilterRunner(
    new File(rootPathFile, "inferences_info").getAbsolutePath,
    new File(rootPathFile, "original_images").getAbsolutePath,
    rootPathFile.getAbsolutePath,
    filterList
  )

  it should "generate a result image by applying all filters" in {
    assert(runner.run())
  }
}
