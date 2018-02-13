package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService}
import com.smartrural.estimator.transformer.ImageTransformer
import com.smartrural.estimator.util.AppConstants.JpgFormat
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
    .flatMap(getBboxesFile)
    .flatMap(bboxService.getDistinctImages)
    .map(file => runTransformers(new File(file)))
    .reduce(_ & _)

  /**
    * Gets the list of child files
    * @param partition the partition to look for files
    * @return the child list
    */
  def getBboxesFile(partition:File):List[File] = {
    if(partition.isFile) List(partition)
    else fileManagerService.getChildList(partition.getAbsolutePath).flatMap(getBboxesFile).toList
  }

  /**
    * Applies the transformer list to the image and saves the result in the mirror path destination
    * @param image the image to transform
    * @return a boolean with the operation
    */
  def runTransformers(image:File):Boolean = {
    import fileManagerService._
    val originalImageBuffer = readImage(image)
    val transformedImage = listFilters.foldLeft(originalImageBuffer)((img, transformer) => transformer.transform(img))
    writeImage(transformedImage, JpgFormat, getMirrorImageFile(image, destinationImagesPath))
  }
}
