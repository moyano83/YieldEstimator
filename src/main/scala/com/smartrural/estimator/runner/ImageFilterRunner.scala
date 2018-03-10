package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService}
import com.smartrural.estimator.transformer.ImageTransformer
import org.slf4j.LoggerFactory
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success, Try}

/**
  * Runnable that applies the different configured filters over the images found in the configured path
  */
class ImageFilterRunner(bboxesPath:String,
                        originalImagesPath:String,
                        destinationImagesPath:String,
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
    * @inheritdoc
    */
  override def run(): Unit = fileManagerService
      .getChildList(bboxesPath)
      .map(bboxService.getDistinctImages)
      .flatMap{case(partition, fileSet) => getImagesFromSetAndBbxFile(partition, fileSet)}
      .foreach(file => {
        Try(runTransformers(file)) match {
          case Success(_) => logger.debug(s"Successfully processed the file=[${file.getName}]")
          case Failure(ex) => logger.error(s"Failed to process the file=[${file.getName}]", ex)
        }
      })


  /**
    * Applies the transformer list to the image and saves the result in the mirror path destination
    * @param image the image to transform
    * @return a boolean with the operation
    */
  def runTransformers(image:File):Unit = {
    import fileManagerService._
    val resultImg = listFilters.foldLeft(readImage(image))((img, transformer) => transformer.transform(image, img))
    writeImage(resultImg, getMirrorImageFile(image, destinationImagesPath))
  }

  /**
    * Gets the set of images
    * @param partition
    * @param imageNames
    * @return
    */
  def getImagesFromSetAndBbxFile(partition:String, imageNames:Set[String]):Set[File] =
    imageNames.map(image => fileManagerService.getComposedFile(List(originalImagesPath, partition, image)))
}
