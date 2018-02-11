package com.smartrural.estimator.service.impl

import java.awt.image.BufferedImage
import java.io.{File, FileInputStream, FilenameFilter}

import com.smartrural.estimator.model.{InferenceInfo, RGBPixel}
import com.smartrural.estimator.service.{ColorDetectionService, FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

class LocalImageReconstructionService(implicit inj:Injector) extends ImageReconstructionService with Injectable{

  val fileManager = inject[FileManagerService]

  val colorDetectionService = inject[ColorDetectionService]

  override def reconstructImage(originalImageFile:File,
                                patchesInfoList:List[InferenceInfo],
                                patchesPath:File,
                                destinationPath:File):Boolean = {
    val originalImage = fileManager.readImage(new FileInputStream(originalImageFile))
    val patchesImages = retrievePatchesForImage(patchesPath.getAbsolutePath, originalImageFile.getName)

    destinationPath.mkdirs()

    createCompleteBinaryImage(
      patchesImages,
      patchesInfoList,
      new BufferedImage(originalImage.getWidth, originalImage.getHeight, BufferedImage.TYPE_INT_RGB),
      new File(destinationPath, originalImageFile.getName)
    )
  }

  override def filterImage(originalImageFile: File, destinationPath: File): Unit = {
    val image = fileManager.readImage(new FileInputStream(originalImageFile))
    val destinationImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB)
    for (x <- 0 until image.getWidth;
         y <- 0 until image.getHeight();
         rgb = new RGBPixel(image.getRGB(x,y))
         if(colorDetectionService.isWithinRange(rgb))
    ) yield writePixel(x,y,rgb.color, destinationImage)

    fileManager.writeImage(destinationImage, AppConstants.JpgFormat, destinationPath)
  }

  def retrievePatchesForImage(patchesPath:String, imageName:String):List[BufferedImage] ={
    val fileFilter = new FilenameFilter {
      override def accept(dir: File, name: String): Boolean =
        name.startsWith(imageName.substring(0, imageName.lastIndexOf(".")))
    }
    fileManager.getChildList(patchesPath)
      .filter(file => fileFilter.accept(file, file.getName))
      .map(patchFile => fileManager.readImage(new FileInputStream(patchFile))).toList
  }

  def createCompleteBinaryImage(patchImagesList: List[BufferedImage],
                                inferenceInfoList:List[InferenceInfo],
                                destinationImage: BufferedImage,
                                destinationFile: File): Boolean = {
    patchImagesList.foreach(image => {
      findMatchingInfoByResolution(inferenceInfoList, getFormattedResolution(image.getWidth, image.getHeight))
        .map(info => writeInferenceImagePixels(image, info, destinationImage))
    })
    if(!patchImagesList.isEmpty) fileManager.writeImage(destinationImage, AppConstants.JpgFormat, destinationFile)
    else false
  }

  private def findMatchingInfoByResolution(inferenceInfoList:List[InferenceInfo],
                                           resolution:String):Option[InferenceInfo] =
    inferenceInfoList.find(_.getResolution == resolution)

  private def writeInferenceImagePixels(image:BufferedImage, info:InferenceInfo, destinationImage: BufferedImage):Unit =
    for (x <- 0 until info.XMaxRange;
         y <- 0 until info.YMaxRange) {
      writePixel(info.getXAdjusted(x), info.getYAdjusted(y), image.getRGB(x, y), destinationImage)
    }

  private def writePixel(xCoordinate:Int, yCoordinate:Int, rgb:Int, destination: BufferedImage):Unit =
    if (colorDetectionService.isNotVoid(rgb)) destination.setRGB(xCoordinate, yCoordinate, rgb)

  private def getFormattedResolution(width:Int, height:Int) = s"$width x $height"
}
