package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.model.VineYieldParameters
import com.smartrural.estimator.service.{FileManagerService, InferenceService}
import com.smartrural.estimator.util.ImageUtils
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

class PixelLocatorRunner(inferencesInfoFile:File,
                         filteredImagePath:String,
                         reconstructedImagesPath:String,
                         destinationResultFile:File,
                         radius:Int)(implicit inj:Injector) extends Injectable with Runnable {

  val fileManagerService = inject[FileManagerService]

  val inferenceService = inject[InferenceService]

  import fileManagerService._

  override def run():Unit =
    getChildList(reconstructedImagesPath)
      .sortBy(_.getAbsolutePath)
      .foreach(file => calculateVineYieldParam(file, readImage(file)))

  private def calculateVineYieldParam(transformedImageFile: File,
                                      transformedImageMat: Mat):Unit = {
    val reconstructedImageMat = readImage(getMirrorImageFile(transformedImageFile, reconstructedImagesPath))
    val percentage = getLeafPixelPercentage(transformedImageMat, reconstructedImageMat)
    val searchFileName = getFileSearchString(transformedImageFile)

    inferenceService
      .getInferenceByPictureName(inferencesInfoFile, searchFileName)
      .foreach(inf => writeObjAsLineToFile(new VineYieldParameters(inf, percentage), destinationResultFile))
  }

  private def getFileSearchString(file:File) = s"${file.getParentFile.getName}/${file.getName}"

  private def getLeafPixelPercentage(transformedImageMat:Mat, reconstructedImageMat:Mat): Double = {
   import ImageUtils._
   getNonVoidPixelCount(transformedImageMat) / getNonVoidPixelCount(reconstructedImageMat)
  }

}
