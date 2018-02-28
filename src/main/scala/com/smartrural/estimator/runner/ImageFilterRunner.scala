package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService}
import com.smartrural.estimator.transformer.ImageTransformer
import scaldi.{Injectable, Injector}

/**
  * Created by jm186111 on 13/02/2018.
  */
class ImageFilterRunner(bboxesPath:String,
                        originalImagesPath:String,
                        destinationImagesPath:String,
                        listFilters:List[ImageTransformer])(implicit inj: Injector)
  extends Runner with Injectable{
  /**
    * BBox service
    */
  val bboxService = inject[BoundingBoxService]
  /**
    * File Manager service
    */
  val fileManagerService = inject[FileManagerService]

  override def run(): Boolean = fileManagerService
      .getChildList(bboxesPath)
      .map(bboxService.getDistinctImages)
      .flatMap{case(partition, fileSet) => getImagesFromSetAndBbxFile(partition, fileSet)}
      .map(runTransformers)
      .reduce(_ & _)

  /**
    * Gets the set of images
    * @param partition
    * @param imageNames
    * @return
    */
  def getImagesFromSetAndBbxFile(partition:String, imageNames:Set[String]):Set[File] =
    imageNames.map(image => fileManagerService.getComposedFile(List(originalImagesPath, partition, image)))

  /**
    * Applies the transformer list to the image and saves the result in the mirror path destination
    * @param image the image to transform
    * @return a boolean with the operation
    */
  def runTransformers(image:File):Boolean = {
    import fileManagerService._
    val resultImg = listFilters.foldLeft(readImage(image))((img, transformer) => transformer.applyTransform(img))
    writeImage(resultImg, getMirrorImageFile(image, destinationImagesPath))
  }
}
