package com.smartrural.estimator.service.impl

import java.awt.image.BufferedImage
import java.io.{File, FileInputStream, FilenameFilter}
import javax.imageio.ImageIO

import com.smartrural.estimator.model.{InferenceInfo, RGBPixel}
import com.smartrural.estimator.service.{BoundingBoxService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

class LocalImageReconstructionService(implicit inj:Injector) extends ImageReconstructionService with Injectable{

  val bboxService = inject[BoundingBoxService]

  override def reconstructImage(originalImageFile:File,
                                bboxesFilePath:File,
                                patchesPath:File,
                                destinationPath:File):Boolean = {
    val originalImage = ImageIO.read(new FileInputStream(originalImageFile))
    val patchesInfoList = bboxService.getFilteredInferenceInfo(bboxesFilePath, originalImageFile.getName)
    val patchesImages = retrievePatchesForImage(patchesPath.getPath, originalImageFile.getName)

    destinationPath.mkdirs()

    createCompleteBinaryImage(
      patchesImages,
      patchesInfoList.toList,
      new BufferedImage(originalImage.getHeight, originalImage.getWidth, BufferedImage.TYPE_INT_RGB),
      new File(destinationPath, originalImageFile.getName)
    )
  }

  def retrievePatchesForImage(patchesPath:String, imageName:String):List[BufferedImage] =
    new File(patchesPath)
      .listFiles(new FilenameFilter {
        override def accept(dir: File, name: String): Boolean =
          name.startsWith(imageName.substring(0, imageName.lastIndexOf(".")))
      }).map(patchFile => ImageIO.read(new FileInputStream(patchFile))).toList

  def createCompleteBinaryImage(patchImagesList: List[BufferedImage],
                                inferenceInfoList:List[InferenceInfo],
                                destinationImage: BufferedImage,
                                destinationFile: File): Boolean = {
    patchImagesList.foreach(image => {
      findMatchingInfoByResolution(inferenceInfoList, s"${image.getWidth} x ${image.getHeight}")
        .map(info => writeInferenceImagePixels(image, info, destinationImage))
    })
    if(!patchImagesList.isEmpty) ImageIO.write(destinationImage, "jpg", destinationFile) else false
  }

  private def findMatchingInfoByResolution(inferenceInfoList:List[InferenceInfo],
                                           resolution:String):Option[InferenceInfo] =
    inferenceInfoList.find(_.toString == resolution)

  private def writeInferenceImagePixels(image:BufferedImage, info:InferenceInfo, destinationImage: BufferedImage):Unit =
    for (x <- 0 until info.XMaxRange;
         y <- 0 until info.YMaxRange) {
      writePixel(info.getXAdjusted(x), info.getYAdjusted(y), image.getRGB(x, y), destinationImage)
    }

  private def writePixel(xCoordinate:Int, yCoordinate:Int, rgb:Int, destination: BufferedImage):Unit =
    if (new RGBPixel(rgb) != AppConstants.VoidRGB) destination.setRGB(xCoordinate, yCoordinate, rgb)
}
