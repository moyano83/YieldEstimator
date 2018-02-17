package com.smartrural.estimator.runner

import java.io.File

import com.smartrural.estimator.service.FileManagerService
import com.smartrural.estimator.transformer.ClusterSurroundingMarkerTransformer
import com.smartrural.estimator.util.ImageUtils
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

class PixelLocatorRunner(originalImagesPath:String,
                         reconstructedImagesPath:String,
                         destinationImagesPath:String,
                         radius:Int)(implicit inj:Injector) extends Injectable with Runner {


  val fileManagerService = inject[FileManagerService]

  val transformer = new ClusterSurroundingMarkerTransformer()

  override def run():Boolean = {
    import fileManagerService._
    getChildList(originalImagesPath)
      .flatMap(partition => getChildList(partition.getAbsolutePath))
      .map(file => (file, readImage(file)))
      .map{
        case (originalImageFile:File, originalImageBuffer:Mat) => {
          val binaryImageBuffer = readImage(getMirrorImageFile(originalImageFile, reconstructedImagesPath))
          val destinationImageFile = getMirrorImageFile(originalImageFile, destinationImagesPath)

          transformImage(originalImageBuffer, binaryImageBuffer, destinationImageFile)
        }
      }.reduce(_ & _)
  }

  def transformImage(originalImageBuffer:Mat,
                     binaryImageBuffer:Mat,
                     destinationImageFile:File):Boolean = {
    val destinationImage = ImageUtils.getMat(originalImageBuffer.rows(), originalImageBuffer.cols())

    fileManagerService.writeImage(destinationImage, destinationImageFile)
  }
}
