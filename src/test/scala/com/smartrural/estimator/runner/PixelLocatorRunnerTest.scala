package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.model._
import com.smartrural.estimator.service.impl.LocalFileManager
import com.smartrural.estimator.service.{FileManagerService, InferenceService}
import org.apache.commons.io.filefilter.TrueFileFilter
import org.junit.runner.RunWith
import org.opencv.core.Core
import org.opencv.imgcodecs.Imgcodecs
import org.scalamock.matchers.MockParameter
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module

/**
  * Created by jm186111 on 01/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class PixelLocatorRunnerTest extends FlatSpec with MockFactory{

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val rootPath = getClass.getClassLoader.getResource(".").getPath
  val rootPathFile = new File(rootPath)

  val inferencesFile = new File(rootPath, "inferences_info/valdemonjas_2017_09_13_inference.geojson")

  val inferenceImageFile = new File(rootPathFile, "Image.png")
  val inferenceImageMat = Imgcodecs.imread(inferenceImageFile.getAbsolutePath)

  val filteredImageFile1 = new File(rootPathFile, "FilteredImage1.png")
  val filteredImageMat1 = Imgcodecs.imread(filteredImageFile1.getAbsolutePath)

  val filteredImageFile2 = new File(rootPathFile, "FilteredImage2.png")
  val filteredImageMat2 = Imgcodecs.imread(filteredImageFile2.getAbsolutePath)

  val fileManager = mock[LocalFileManager]
  val inferenceService = mock[InferenceService]

  val resultFile = new File(rootPath, "result.txt")



  def setExpectations1():Unit= {
    val result:MockParameter[Any] =
      VineYieldParameters("test-classes/FilteredImage1.png", 1,-4.283807,41.623104,36,10, 0.5)
    val mockResultFile:MockParameter[File] = resultFile

    (fileManager.getChildList _)
      .expects(rootPath, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)
      .returns(Array(filteredImageFile1)).anyNumberOfTimes()
    (fileManager.readImage _).expects(filteredImageFile1).returns(filteredImageMat1)
    (fileManager.getMirrorImageFile _).expects(*, *).returns(inferenceImageFile)
    (fileManager.readImage _).expects(inferenceImageFile).returns(inferenceImageMat)
    (fileManager.writeObjAsLineToFile _).expects(result, mockResultFile).returns(true).once

    (inferenceService.getInferenceByPictureName _)
      .expects(inferencesFile, "test-classes/FilteredImage1.png")
      .returns(Some(InferenceInfo(
    "Feature", InferenceGeometry("Point", Array(-4.283807,41.623104)),
          InferenceProperties(1, "test-classes/FilteredImage1.png", 10, 10, 36, 1, 10))
      )).once
  }

  def setExpectations2():Unit= {
    val result:MockParameter[Any] =
      VineYieldParameters("test-classes/FilteredImage2.png", 1,-4.283807,41.623104,36,10, 0.25)
    val mockResultFile:MockParameter[File] = resultFile

    (fileManager.getChildList _)
      .expects(rootPath, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)
      .returns(Array(filteredImageFile2)).anyNumberOfTimes()
    (fileManager.readImage _).expects(filteredImageFile2).returns(filteredImageMat2)
    (fileManager.getMirrorImageFile _).expects(*, *).returns(inferenceImageFile)
    (fileManager.readImage _).expects(inferenceImageFile).returns(inferenceImageMat)
    (fileManager.writeObjAsLineToFile _).expects(result, mockResultFile).returns(true).once

    (inferenceService.getInferenceByPictureName _)
      .expects(inferencesFile, "test-classes/FilteredImage2.png")
      .returns(Some(InferenceInfo(
        "Feature", InferenceGeometry("Point", Array(-4.283807,41.623104)),
        InferenceProperties(1, "test-classes/FilteredImage2.png", 10, 10, 36, 1, 10))
      )).once
  }

  val radius = 5

  implicit val inj = new Module{
    bind[FileManagerService] to fileManager
    bind[InferenceService] to inferenceService
  }

  behavior of "ImageFilterRunner"

  val runner = new PixelLocatorRunner(inferencesFile, rootPath, rootPath, resultFile, radius)

  it should "generate a result file by applying all filters for the 50% occlussion factor" in {
    resultFile.delete()
    setExpectations1()
    assert(runner.run())
  }

  it should "generate a result file by applying all filters for the 25% occlussion factor" in {
    resultFile.delete()
    setExpectations2()
    assert(runner.run())
  }
}
