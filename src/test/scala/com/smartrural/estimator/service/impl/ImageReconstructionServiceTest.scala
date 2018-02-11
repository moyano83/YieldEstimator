package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, ColorDetectionService, FileManagerService}
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module


@RunWith(classOf[JUnitRunner])
class ImageReconstructionServiceTest extends FlatSpec with MockFactory{

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)
  val partition = "valdemonjas-2017-09-13_01"
  val patchesFolder = new File(rootPathFile, s"inferences/${partition}")
  val originalImagesFolder =  new File(rootPathFile, s"original_images/${partition}")
  val bboxesFolder =  new File(rootPathFile, s"inferences_info/${partition}")
  val imageName = "z-img-000-000004.jpg"

  val colorService = mock[ColorDetectionService]
  val bboxService = new BoundingBoxTextReaderService
  implicit val inj = new Module{
    bind[BoundingBoxService] to bboxService
    bind[FileManagerService] to new LocalFileManager
    bind[ColorDetectionService] to new BoundedColorDetectionService(Range(45, 170), Range(0,100), Range(0,100))
  }
  val imageReconstructionService = new LocalImageReconstructionService()

  behavior of "ImageReconstructionService"

  it should "find the related images within the inferences folder" in {
    val bufferedImageArray = imageReconstructionService.retrievePatchesForImage(patchesFolder.getPath, imageName)
    assert(bufferedImageArray.size == 7)
    assert(bufferedImageArray(0).getWidth == 91)
    assert(bufferedImageArray(0).getHeight == 145)
    assert(bufferedImageArray(1).getWidth == 57)
    assert(bufferedImageArray(1).getHeight == 65)
    assert(bufferedImageArray(2).getWidth == 63)
    assert(bufferedImageArray(2).getHeight == 111)
    assert(bufferedImageArray(3).getWidth == 31)
    assert(bufferedImageArray(3).getHeight == 53)
    assert(bufferedImageArray(4).getWidth == 97)
    assert(bufferedImageArray(4).getHeight == 132)
    assert(bufferedImageArray(5).getWidth == 61)
    assert(bufferedImageArray(5).getHeight == 61)
    assert(bufferedImageArray(6).getWidth == 59)
    assert(bufferedImageArray(6).getHeight == 52)
  }

  it should "reconstruct the final image from the available patches" in {
    val destinationFolder = new File(rootPathFile, "results")
    val imageFile = new File(originalImagesFolder, imageName)
    val inferences = bboxService.readBBoxFile(bboxesFolder).get(imageName).get

    assert(imageReconstructionService.reconstructImage(imageFile, inferences, patchesFolder, destinationFolder))
    assert(new File(destinationFolder, imageName).exists())
  }

  it should "filter the pixels of an image according to the configured filter" in {
    val imageFile = new File(originalImagesFolder, imageName)
    val destinationImage = new File(rootPathFile, imageName)
    setColorServiceExpectations()
    imageReconstructionService.filterImage(imageFile, destinationImage)
    assert(destinationImage.exists())
  }

  def setColorServiceExpectations():Unit = {
    (colorService.isWithinRange _).expects(*).returns(true).anyNumberOfTimes()
  }
}







