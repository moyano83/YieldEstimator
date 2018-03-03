package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

class ImageReconstructionRunner(bboxesPath:String,
                                originalImagesPath:String,
                                patchImgPath:String,
                                destinationPath:String)(implicit inj:Injector) extends Injectable with Runnable{

  val imageReconstructionService = inject[ImageReconstructionService]

  val fileManagerService = inject[FileManagerService]

  val boundingBoxService = inject[BoundingBoxService]

  override def run():Unit = fileManagerService
      .getChildList(bboxesPath)
      .foreach(reconstructImagesPerPartition)


  def reconstructImagesPerPartition(bboxFile:File):Boolean =
    boundingBoxService.readBBoxFile(bboxFile)
    .map({case (image, inferenceList) =>{
      val partition = bboxFile.getParentFile.getName
        imageReconstructionService.reconstructImage(
          fileManagerService.getComposedFile(List(originalImagesPath, partition, image)),
          inferenceList,
          fileManagerService.getComposedFile(List(patchImgPath, partition)),
          fileManagerService.getComposedFile(List(destinationPath, partition)))
      }
    }).foldLeft(true)(_ & _)

}
