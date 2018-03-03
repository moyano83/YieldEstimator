package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

/**
  * Class that d
  * @param bboxesPath
  * @param originalImagesPath
  * @param patchImgPath
  * @param destinationPath
  * @param inj
  */
class ImageReconstructionRunner(bboxesPath:String,
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
        imageReconstructionService.reconstructImage(
          fileManagerService.getComposedFile(List(originalImagesPath, partition, image)),
          inferenceList,
          fileManagerService.getComposedFile(List(patchImgPath, partition)),
          fileManagerService.getComposedFile(List(destinationPath, partition)))
      }
    })

}
