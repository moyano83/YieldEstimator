package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.transformer.impl.{ClusterSurroundingFilterTransformer, EnforcePixelColorFilterTransformer}
import scaldi.{Injectable, Injector}

/**
  * Class that creates the final binary image from the patches described in the bounding box info file
  * @param radius The radius to consider
  * @param bboxesPath the root path to the bbox files
  * @param originalImagesPath the root path to the original images
  * @param patchImgPath the root path to the patch image files
  * @param destinationPath the root path of the destination images
  * @param inj the scaldi dependency injector module
  */
class ImageReconstructionRunner(radius:Int,
                                bboxesPath:String,
                                originalImagesPath:String,
                                patchImgPath:String,
                                destinationPath:String)(implicit inj:Injector) extends Injectable with Runnable{

  /**
    * The image reconstruction service
    */
  val imageReconstructionService = inject[ImageReconstructionService]
  /**
    * The file manager service
    */
  val fileManagerService = inject[FileManagerService]
  /**
    * The bounding box service
    */
  val boundingBoxService = inject[BoundingBoxService]

  /**
    * The list of filters to apply
    */
  val filterList = List(new ClusterSurroundingFilterTransformer(radius.toInt), new EnforcePixelColorFilterTransformer)
  /**
    * @inheritdoc
    */
  override def run():Unit = fileManagerService
      .getChildList(bboxesPath)
      .foreach(reconstructImagesPerPartition)
  /**
    * Reconstructs all the images described in the bbox file passed
    * @param bboxFile the bbox file containing the image information
    */
  def reconstructImagesPerPartition(bboxFile:File):Unit =
    boundingBoxService.readBBoxFile(bboxFile)
      .map({case (image, inferenceList) =>{
      val partition = bboxFile.getParentFile.getName
        imageReconstructionService.reconstructImage(radius,
          fileManagerService.getComposedFile(List(originalImagesPath, partition, image)),
          inferenceList,
          fileManagerService.getComposedFile(List(patchImgPath, partition)),
          filterList,
          fileManagerService.getComposedFile(List(destinationPath, partition)))
      }
    })
}
