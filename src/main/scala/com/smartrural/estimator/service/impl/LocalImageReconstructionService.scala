package com.smartrural.estimator.service.impl

import java.awt.image.BufferedImage
import java.io.{File, FileInputStream, FilenameFilter}

import com.smartrural.estimator.model.{ColoredPixel, InferenceInfo}
import com.smartrural.estimator.service.{FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants.{RedColor, VoidColor}
import org.opencv.core.{CvType, Mat}
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
      new Mat(originalImage.getWidth, originalImage.getHeight, CvType.CV_8UC3),
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
                                destinationImage: Mat,
                                destinationFile: File): Boolean = {
    patchImagesList.foreach(image => {
      findMatchingInfoByResolution(inferenceInfoList,
        fileManager.getFormattedResolution(image))
        .map(info => writeInferenceImagePixels(image, info, destinationImage))
    })

    if(patchImagesList.isEmpty) false
    else fileManager.writeImage(destinationImage, destinationFile)
  }

  private def findMatchingInfoByResolution(inferenceInfoList:List[InferenceInfo],
                                           resolution:String):Option[InferenceInfo] =
    inferenceInfoList.find(_.getResolution == resolution)

  private def writeInferenceImagePixels(img:BufferedImage, info:InferenceInfo, destinationImg: Mat):Unit =
    for (x <- 0 until info.XMaxRange;
         y <- 0 until info.YMaxRange) {
      destinationImg.put(info.getXAdjusted(x), info.getYAdjusted(y),
        if(new ColoredPixel(img,x,y).isVoid()) VoidColor else RedColor)
    }
}
