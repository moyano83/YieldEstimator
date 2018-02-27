package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService}
import com.smartrural.estimator.util.AppConstants
import org.junit.runner.RunWith
import org.opencv.core.Core
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module


@RunWith(classOf[JUnitRunner])
class ImageReconstructionServiceTest extends FlatSpec with MockFactory{

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)
  val partition = "valdemonjas-2017-09-13_01"
  val patchesFolder = new File(rootPathFile, s"inferences/${partition}")
  val originalImagesFolder =  new File(rootPathFile, s"original_images/${partition}")
  val bboxesFolder =  new File(rootPathFile, s"bbox_info/${partition}/${AppConstants.BbBoxesFileName}")
  val imageName = "z-img-000-000004.jpg"
  val destImageName = "z-img-000-000004.png"

  val bboxService = new BoundingBoxTextReaderService
  implicit val inj = new Module{
    bind[BoundingBoxService] to bboxService
    bind[FileManagerService] to new LocalFileManager
  }
  val imageReconstructionService = new LocalImageReconstructionService()

  behavior of "ImageReconstructionService"

  it should "find the related images within the inferences folder" in {
    val bufferedImageArray = imageReconstructionService.retrievePatchesForImage(patchesFolder.getPath, imageName)
    assert(bufferedImageArray.size == 7)
    assert(bufferedImageArray(0).width == 91)
    assert(bufferedImageArray(0).height == 145)
    assert(bufferedImageArray(1).width == 57)
    assert(bufferedImageArray(1).height == 65)
    assert(bufferedImageArray(2).width == 63)
    assert(bufferedImageArray(2).height == 111)
    assert(bufferedImageArray(3).width == 31)
    assert(bufferedImageArray(3).height == 53)
    assert(bufferedImageArray(4).width == 97)
    assert(bufferedImageArray(4).height == 132)
    assert(bufferedImageArray(5).width == 61)
    assert(bufferedImageArray(5).height == 61)
    assert(bufferedImageArray(6).width == 59)
    assert(bufferedImageArray(6).height == 52)
  }

  it should "reconstruct the final image from the available patches" in {
    val destinationFolder = new File(rootPathFile, "results")
    val imageFile = new File(originalImagesFolder, imageName)
    val inferences = bboxService.readBBoxFile(bboxesFolder).get(imageName).get

    assert(imageReconstructionService.reconstructImage(imageFile, inferences, patchesFolder, destinationFolder))
    assert(new File(destinationFolder, destImageName).exists())
  }

}







