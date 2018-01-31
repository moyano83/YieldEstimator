package com.smartrural.estimator.runner

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.service.PixelLocatorService
import com.smartrural.estimator.util.{AppConstants, FileUtils}
import scaldi.{Injectable, Injector}

class PixelLocatorRunner(originalImagesPath:String,
                         reconstructedImagesPath:String,
                         destinationImagesPath:String,
                         radius:Int)(implicit inj:Injector) extends Injectable with Runner {

  val pixelLocatorService = inject[PixelLocatorService]

  override def run():Boolean = {
    FileUtils.getChildList(originalImagesPath)
      .flatMap(partition => FileUtils.getChildList(partition.getAbsolutePath))
      .map(originalImageFile => {
        generateImageWithJustSurroundingClusterPixels(
          originalImageFile,
          getMirrorImageFile(originalImageFile, reconstructedImagesPath),
          getMirrorImageFile(originalImageFile, destinationImagesPath))
      }).reduce(_&_)
  }

  def getMirrorImageFile(imageToMirror:File, mirrorBasePath:String):File = {
    val imageName = imageToMirror.getName
    val partitionFolder = imageToMirror.getParentFile.getName
    new File(new File(mirrorBasePath, partitionFolder), imageName)
  }

  def generateImageWithJustSurroundingClusterPixels(originalImage:File,
                                                    binaryImage:File,
                                                    destinationImageFile:File):Boolean = {
    val pixelsToCopy = pixelLocatorService.findSurroundingClusterPixels(binaryImage, radius)
    val originalImageBuffer = ImageIO.read(originalImage)
    val destinationImage = ImageIO.read(destinationImageFile)
    pixelsToCopy.map(coord => destinationImage.setRGB(coord.x, coord.y, originalImageBuffer.getRGB(coord.x, coord.y)))
    ImageIO.write(destinationImage, AppConstants.JpgFormat, destinationImageFile)
  }
}
