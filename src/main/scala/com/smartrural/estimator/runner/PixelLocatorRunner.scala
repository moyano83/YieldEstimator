package com.smartrural.estimator.runner

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.service.{FileManagerService, PixelLocatorService}
import com.smartrural.estimator.util.AppConstants
import scaldi.{Injectable, Injector}

class PixelLocatorRunner(originalImagesPath:String,
                         reconstructedImagesPath:String,
                         destinationImagesPath:String,
                         radius:Int)(implicit inj:Injector) extends Injectable with Runner {

  val pixelLocatorService = inject[PixelLocatorService]

  val fileManagerService = inject[FileManagerService]

  override def run():Boolean = {
    fileManagerService.getChildList(originalImagesPath)
      .flatMap(partition => fileManagerService.getChildList(partition.getAbsolutePath))
      .map(originalImageFile => {
        generateImageWithJustSurroundingClusterPixels(
          originalImageFile,
          fileManagerService.getMirrorImageFile(originalImageFile, reconstructedImagesPath),
          fileManagerService.getMirrorImageFile(originalImageFile, destinationImagesPath))
      }).reduce(_&_)
  }

  def generateImageWithJustSurroundingClusterPixels(originalImage:File,
                                                    binaryImage:File,
                                                    destinationImageFile:File):Boolean = {
    val pixelsToCopy = pixelLocatorService.findSurroundingClusterPixels(binaryImage, radius)
    val originalImageBuffer = ImageIO.read(originalImage)
    val destinationImage =
      new BufferedImage(originalImageBuffer.getHeight, originalImageBuffer.getWidth, BufferedImage.TYPE_INT_RGB)
    pixelsToCopy.map(coord => destinationImage.setRGB(coord.x, coord.y, originalImageBuffer.getRGB(coord.x, coord.y)))
    ImageIO.write(destinationImage, AppConstants.JpgFormat, destinationImageFile)
  }
}
