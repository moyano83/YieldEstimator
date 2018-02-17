package com.smartrural.estimator.service.impl

import java.io.{File, FilenameFilter}

import com.smartrural.estimator.model.{ColoredPixel, InferenceInfo}
import com.smartrural.estimator.service.{FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants.{RedColor, VoidColor}
import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

class LocalImageReconstructionService(implicit inj:Injector) extends ImageReconstructionService with Injectable{

  val fileManager = inject[FileManagerService]

  override def reconstructImage(originalImageFile:File,
                                patchesInfoList:List[InferenceInfo],
                                patchesPath:File,
                                destinationPath:File):Boolean = {
    val originalImage = fileManager.readImage(originalImageFile)
    val patchesImages = retrievePatchesForImage(patchesPath.getAbsolutePath, originalImageFile.getName)

    destinationPath.mkdirs()

    createCompleteBinaryImage(
      patchesImages,
      patchesInfoList,
      getMat(originalImage.rows(), originalImage.cols()),
      new File(destinationPath, originalImageFile.getName.replace(".jpg", ".png"))
    )
  }

  def retrievePatchesForImage(patchesPath:String, imageName:String):List[Mat] ={
    val fileFilter = new FilenameFilter {
      override def accept(dir: File, name: String): Boolean =
        name.startsWith(imageName.substring(0, imageName.lastIndexOf(".")).concat("_"))
    }
    fileManager.getChildList(patchesPath)
      .filter(file => fileFilter.accept(file, file.getName))
      .map(patchFile => fileManager.readImage(patchFile)).toList
  }

  def createCompleteBinaryImage(patchImagesList: List[Mat],
                                inferenceInfoList:List[InferenceInfo],
                                destinationImage: Mat,
                                destinationFile: File): Boolean = {
    patchImagesList.foreach(patchImage => {
      findMatchingInfoByResolution(inferenceInfoList, getFormattedResolution(patchImage.rows, patchImage.cols))
        .map(info => writeInferenceImagePixels(patchImage, info, destinationImage))
    })

    if(patchImagesList.isEmpty) false
    else fileManager.writeImage(destinationImage, destinationFile)
  }

  private def findMatchingInfoByResolution(inferenceInfoList:List[InferenceInfo],
                                           resolution:String):Option[InferenceInfo] =
    inferenceInfoList.find(_.getResolution == resolution)

  private def writeInferenceImagePixels(img:Mat, info:InferenceInfo, dst: Mat):Unit =
    getMatAsColoredPixels(img)
      .map(pixel => dst.put(info.getRowAdjusted(pixel.row), info.getColAdjusted(pixel.col), getColorForPixel(pixel)))

  private def getColorForPixel(pixel:ColoredPixel) = if (pixel.isVoid()) VoidColor else RedColor
}
