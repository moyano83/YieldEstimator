package com.smartrural.estimator.runner

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage._
import java.io.File

import com.smartrural.estimator.service.{FileManagerService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

class PixelLocatorRunner(originalImagesPath:String,
                         reconstructedImagesPath:String,
                         destinationImagesPath:String,
                         radius:Int)(implicit inj:Injector) extends Injectable with Runner {


  val fileManagerService = inject[FileManagerService]

  override def run():Boolean = {
    import fileManagerService._
    getChildList(originalImagesPath)
      .flatMap(partition => getChildList(partition.getAbsolutePath))
      .map(file => (file, readImage(file)))
      .map{
        case (originalImageFile:File, originalImageBuffer:BufferedImage) => {
          val binaryImageBuffer = readImage(getMirrorImageFile(originalImageFile, reconstructedImagesPath))
          val destinationImageFile = getMirrorImageFile(originalImageFile, destinationImagesPath)

          transformImage(originalImageBuffer, binaryImageBuffer, destinationImageFile)
        }
      }.reduce(_ & _)
  }

  def transformImage(originalImageBuffer:BufferedImage,
                     binaryImageBuffer:BufferedImage,
                     destinationImageFile:File):Boolean = {
    val destinationImage = new BufferedImage(originalImageBuffer.getHeight, originalImageBuffer.getWidth, TYPE_INT_RGB)
    //pixelsToCopy.foreach(px => destinationImage.setRGB(px.x, px.y, originalImageBuffer.getRGB(px.x, px.y)))
    fileManagerService.writeImage(destinationImage, AppConstants.JpgFormat, destinationImageFile)
  }
}
