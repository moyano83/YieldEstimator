package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.model.VineYieldParameters
import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService, InferenceService}
import com.smartrural.estimator.transformer.ImageTransformer
import com.smartrural.estimator.util.AppConstants._
import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core.Mat
import org.slf4j.LoggerFactory
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success, Try}

/**
  * Runnable that applies the different configured filters over the images found in the configured path
  */
class ImageFilterRunner(inferencesInfoFile:File,
                        maskImagesPath:String,
                        originalImagesPath:String,
                        destinationFilePath:String,
                        listFilters:List[ImageTransformer])(implicit inj: Injector)
  extends Runnable with Injectable{

  /**
    * Class logger
    */
  val logger = LoggerFactory.getLogger(getClass)
  /**
    * BBox service
    */
  val bboxService = inject[BoundingBoxService]
  /**
    * File Manager service
    */
  val fileManagerService = inject[FileManagerService]
  /**
    * Inference Service
    */
  val inferenceService = inject[InferenceService]

  import fileManagerService._
  /**
    * @inheritdoc
    */
  override def run(): Unit = fileManagerService
      .getChildList(maskImagesPath)
      .filter(!getMirrorFileWithExtension(_, destinationFilePath, TextExtension).exists())
      .map(getMirrorFileWithExtension(_, originalImagesPath, FormatJpg))
      .foreach(file => {
        Try(listFilters.foldLeft(readImage(file))((img, transformer) => transformer.transform(file, img))) match {
          case Success(mat) => calculateAndWriteVineYieldParam(file, mat)
          case Failure(ex) => logger.error(s"Failed to process the file=[${file.getName}]", ex)
        }
      })

  /**
    * Claculates the Yield parameters and writes it to the output file
    * @param imgFile the image file
    * @param imgMat the image Mat corresponding to the file
    */
  private def calculateAndWriteVineYieldParam(imgFile: File, imgMat: Mat):Unit = {
    val reconstructedImageMat = readImage(getMirrorFileWithExtension(imgFile, maskImagesPath, FormatPng))
    val destinationFile = getMirrorFileWithExtension(imgFile, destinationFilePath, TextExtension)
    val percentage = getLeafPixelPercentage(imgMat, reconstructedImageMat)
    val searchFileName = getFileSearchString(imgFile)

    inferenceService
      .getInferenceByPictureName(inferencesInfoFile, searchFileName)
      .foreach(inf => writeObjAsLineToFile(new VineYieldParameters(inf, percentage), destinationFile))
  }

  /**
    * Gets a string to search for the files
    * @param file the file to compose the string
    * @return the search string
    */
  private def getFileSearchString(file:File) =
    s"${file.getParentFile.getName}/${file.getName}"

  /**
    * Gets the leaf percentage in the math
    * @param transformedImageMat the image we look for leafs
    * @param maskMat the mask mat for that picture
    * @return the leaf percentage
    */
  private def getLeafPixelPercentage(transformedImageMat:Mat, maskMat:Mat): Double =
    getNonVoidPixelCount(transformedImageMat) / getNonVoidPixelCount(maskMat)
}
