package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.model.{ColoredPixel, InferenceInfo, VineYieldParameters}
import com.smartrural.estimator.service.{FileManagerService, InferenceService}
import com.smartrural.estimator.util.AppConstants.VoidColor
import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

class PixelLocatorRunner(inferencesFile:File,
                         filteredImagePath:String,
                         reconstructedImagesPath:String,
                         destinationResultFile:File,
                         radius:Int)(implicit inj:Injector) extends Injectable with Runner {

  val fileManagerService = inject[FileManagerService]

  val inferenceService = inject[InferenceService]

  import fileManagerService._

  override def run():Boolean =
    getChildList(filteredImagePath)
      .flatMap(partition => getChildList(partition.getAbsolutePath))
      .map(file => (file, readImage(file)))
      .map({
        case (transformedImageFile:File, transformedImageMat:Mat) => {
          calculateVineYieldParam(transformedImageFile, transformedImageMat)
        }
      })
      .flatten
      .map(vineParameters => {
        fileManagerService.writeObjAsLineToFile(vineParameters, destinationResultFile)
      })
      .foldLeft(true)(_ & _)

  private def calculateVineYieldParam(transformedImageFile: File, transformedImageMat: Mat) = {
    val binaryImageBuffer = readImage(getMirrorImageFile(transformedImageFile, reconstructedImagesPath))
    val percentage = getLeafPixelPercentage(transformedImageMat, binaryImageBuffer)
    val inference = inferenceService.getInferenceByPictureName(inferencesFile, getFileSearchString(transformedImageFile))
    inference.map(inf => new VineYieldParameters(inf, percentage))
  }

  private def getFileSearchString(file:File) = s"${file.getParentFile.getName}/${file.getName}"

  private def getLeafPixelPercentage(transformedImageMat:Mat, binaryImageMat:Mat):Double = {
    val pixels = extractSurroundingPixels(binaryImageMat,radius)
    pixels.map(px => arrayToColoredPixel(binaryImageMat, px.row, px.col)).filter(_.isVoid).size / pixels.size.toDouble
  }

}
