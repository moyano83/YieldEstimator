package com.smartrural.estimator.service.impl

import java.awt.image.BufferedImage
import java.io.{File, FileInputStream, FilenameFilter}

import com.smartrural.estimator.model.{ColoredPixel, InferenceInfo}
import com.smartrural.estimator.service.{FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

class LocalImageReconstructionService(implicit inj:Injector) extends ImageReconstructionService with Injectable{

  val fileManager = inject[FileManagerService]

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
      new File(destinationPath, originalImageFile.getName.replace(".jpg", ".png"))
    )
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
      findMatchingInfoByResolution(inferenceInfoList,
        fileManager.getFormattedResolution(image))
        .map(info => writeInferenceImagePixels(image, info, destinationImage))
    })

    if(patchImagesList.isEmpty) false
    else fileManager.writeImage(destinationImage, AppConstants.PngFormat, destinationFile)
  }

  private def findMatchingInfoByResolution(inferenceInfoList:List[InferenceInfo],
                                           resolution:String):Option[InferenceInfo] =
    inferenceInfoList.find(_.getResolution == resolution)

  private def writeInferenceImagePixels(img:BufferedImage, info:InferenceInfo, destinationImg: BufferedImage):Unit =
    for (x <- 0 until info.XMaxRange;
         y <- 0 until info.YMaxRange) {
      destinationImg.setRGB(info.getXAdjusted(x), info.getYAdjusted(y),
        if(new ColoredPixel(img,x,y).isVoid()) 0 else AppConstants.RedColor)
    }
}
