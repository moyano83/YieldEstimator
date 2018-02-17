package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.{BoundingBoxService, FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

class ImageReconstructionRunner(bboxesPath:String,
                                originalImagesPath:String,
                                patchImgPath:String,
                                destinationPath:String)(implicit inj:Injector) extends Injectable with Runner{

  val imageReconstructionService = inject[ImageReconstructionService]

  val fileManagerService = inject[FileManagerService]

  val boundingBoxService = inject[BoundingBoxService]

  override def run() = fileManagerService
      .getChildList(bboxesPath)
      .map(partition => reconstructImagesPerPartition(partition.getName))
      .foldLeft(true)(_ & _)

  def reconstructImagesPerPartition(partition:String):Boolean = fileManagerService
    .getChildList(new File(bboxesPath, partition).getAbsolutePath)
    .filter(_.getName == AppConstants.BbBoxesFileName)
    .map(bboxFile => boundingBoxService.readBBoxFile(bboxFile))
    .flatMap(imageMap =>
      imageMap.map { case (image, inferenceList) =>
        imageReconstructionService.reconstructImage(
          fileManagerService.getComposedFile(List(originalImagesPath, partition, image)),
          inferenceList,
          fileManagerService.getComposedFile(List(patchImgPath, partition)),
          fileManagerService.getComposedFile(List(destinationPath, partition))
        )
      }
    ).foldLeft(true)(_ & _)

}
