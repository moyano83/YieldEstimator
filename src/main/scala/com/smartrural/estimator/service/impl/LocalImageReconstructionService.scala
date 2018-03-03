package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.model.{BBoxItemInfo, ColoredPixel}
import com.smartrural.estimator.service.{FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants.{RedColor, VoidColor}
import com.smartrural.estimator.util.ImageUtils._
import org.apache.commons.io.filefilter.IOFileFilter
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

class LocalImageReconstructionService(implicit inj:Injector) extends ImageReconstructionService with Injectable{

  val fileManager = inject[FileManagerService]

  override def reconstructImage(originalImageFile:File,
                                patchesInfoList:List[BBoxItemInfo],
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
    val fileFilter = new IOFileFilter {
      override def accept(file: File) =
        file.getName.startsWith(imageName.substring(0, imageName.lastIndexOf(".")).concat("_"))
      override def accept(dir: File, name: String) = accept(new File(dir, name))
    }

    fileManager.getChildList(patchesPath, fileFilter).map(patchFile => fileManager.readImage(patchFile)).toList
  }

  def createCompleteBinaryImage(patchImagesList: List[Mat],
                                inferenceInfoList:List[BBoxItemInfo],
                                destinationImage: Mat,
                                destinationFile: File): Boolean = {
    inferenceInfoList.foreach(inferenceInfo => {
      findMatchingMatByResolution(patchImagesList, inferenceInfo)
        .map(mat => writeInferenceImagePixels(mat, inferenceInfo, destinationImage))
    })

    if(patchImagesList.isEmpty) false
    else fileManager.writeImage(destinationImage, destinationFile)
  }

  private def findMatchingMatByResolution(patchImagesList:List[Mat],
                                          info:BBoxItemInfo):Option[Mat] =
    patchImagesList.find(mat => mat.rows == info.ImageHeight && mat.cols == info.ImageWidth)

  private def writeInferenceImagePixels(img:Mat, info:BBoxItemInfo, dst: Mat):Unit =
    getMatAsColoredPixels(img)
      .map(pixel => dst.put(info.getRowAdjusted(pixel.row), info.getColAdjusted(pixel.col), getColorForPixel(pixel)))

  private def getColorForPixel(pixel:ColoredPixel) = if (pixel.isVoid()) VoidColor else RedColor
}
