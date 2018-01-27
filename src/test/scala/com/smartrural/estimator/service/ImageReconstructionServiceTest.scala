package com.smartrural.estimator.service

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.AppConstants
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ImageReconstructionServiceTest extends FlatSpec{

  val rootPath = getClass.getClassLoader.getResource(".").getPath
  val patchesFolder = new File(new File(rootPath), "inferences/valdemonjas-2017-09-13_01")
  val originalImagesFolder =  new File(rootPath.concat("original_images/valdemonjas-2017-09-13_01"))
  val bboxesFolder =  new File(rootPath.concat("inferences_info/valdemonjas-2017-09-13_01"))
  val imageName = "z-img-000-000004.png"
  val image = ImageIO.read(getClass.getClassLoader.getResourceAsStream("./"))

  behavior of "ImageReconstructionService"

  it should "find the related images within the inferences folder" in {
    val bufferedImageArray =
      ImageReconstructionService.retrievePatchesForImage(patchesFolder.getPath, imageName)
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
    ImageReconstructionService.reconstructImage(
      new File(originalImagesFolder, imageName),
      new File(bboxesFolder, AppConstants.BbBoxesFileName),
      patchesFolder,
      rootPath
    )

    assert(new File(rootPath, imageName).exists())
  }

  it should "recreate the binary image from the patches" in {

  }

  it should "write the pixel with the specified value" in {

  }

  it should "not write the pixel if it is void" in {

  }
}
