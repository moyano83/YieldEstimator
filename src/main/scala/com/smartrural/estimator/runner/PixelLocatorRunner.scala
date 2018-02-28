package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.model.VineYieldParameters
import com.smartrural.estimator.service.{FileManagerService, InferenceService}
import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

class PixelLocatorRunner(inferencesInfoFile:File,
                         filteredImagePath:String,
                         reconstructedImagesPath:String,
                         destinationResultFile:File,
                         radius:Int)(implicit inj:Injector) extends Injectable with Runner {

  val fileManagerService = inject[FileManagerService]

  val inferenceService = inject[InferenceService]

  import fileManagerService._

  override def run():Boolean =
    getChildList(reconstructedImagesPath)
      .map(file => calculateVineYieldParam(file, readImage(file)))
      .map(vineYieldResult => writeObjAsLineToFile(vineYieldResult, destinationResultFile))
      .foldLeft(true)(_ & _)

  private def calculateVineYieldParam(transformedImageFile: File,
                                      transformedImageMat: Mat):Option[VineYieldParameters] = {
    val binaryImageBuffer = readImage(getMirrorImageFile(transformedImageFile, reconstructedImagesPath))
    val percentage = getLeafPixelPercentage(transformedImageMat, binaryImageBuffer)
    val searchFileName = getFileSearchString(transformedImageFile)

    inferenceService
      .getInferenceByPictureName(inferencesInfoFile, searchFileName)
      .map(inf => new VineYieldParameters(inf, percentage))
  }

  private def getFileSearchString(file:File) = s"${file.getParentFile.getName}/${file.getName}"

  private def getLeafPixelPercentage(transformedImageMat:Mat, binaryImageMat:Mat): Double = {
    val listPositions = extractSurroundingLeafCoordinates(transformedImageMat, binaryImageMat)

    val transformedImagePixelsWithIndex =
      getMatAsColoredPixels(transformedImageMat).zipWithIndex.filter(tp => listPositions.contains(tp._2)).map(_._1)

    if(transformedImagePixelsWithIndex.size == 0) 0d
    else transformedImagePixelsWithIndex.filter(_.isNotVoid()).size / transformedImagePixelsWithIndex.size.toDouble
  }

  private def extractSurroundingLeafCoordinates(transformedImageMat:Mat, binaryImageMat: Mat) =
    extractAllSurroundingVoidPixelsFromImage(binaryImageMat, radius)
      .map(px => getListPositionByCoordinate(transformedImageMat.cols, px.row, px.col))
}
