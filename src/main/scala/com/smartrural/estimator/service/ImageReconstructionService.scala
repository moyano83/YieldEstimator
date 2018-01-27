package com.smartrural.estimator.service

import java.awt.image.BufferedImage
import java.io.{File, FileInputStream, FilenameFilter}
import javax.imageio.ImageIO

import com.smartrural.estimator.AppConstants
import com.smartrural.estimator.file.BoundingBoxTextReader
import com.smartrural.estimator.model.{InferenceInfo, RGBPixel}

object ImageReconstructionService {

  def reconstructImage(originalImageFile:File, bboxesInfoFile:File, patchesPath:File, destinationPath:String):Boolean = {
    val partitionFolder = originalImageFile.getParentFile.getName
    val originalImage = ImageIO.read(new FileInputStream(originalImageFile))
    val patchesInfoList = BoundingBoxTextReader.getFilteredInferenceInfo(bboxesInfoFile, originalImageFile.getName)
    val patchesImages = retrievePatchesForImage(patchesPath.getPath, originalImageFile.getName)

    createCompleteBinaryImage(
      patchesImages.zip(patchesInfoList.toList),
      new BufferedImage(originalImage.getHeight, originalImage.getWidth, BufferedImage.TYPE_INT_RGB),
      new File(new File(destinationPath, partitionFolder), originalImageFile.getName)
    )
  }

  def retrievePatchesForImage(patchesPath:String, imageName:String):List[BufferedImage] =
    new File(patchesPath)
      .listFiles(new FilenameFilter {
        override def accept(dir: File, name: String): Boolean =
          name.startsWith(imageName.substring(0, imageName.lastIndexOf(".png")))
      }).map(patchFile => ImageIO.read(new FileInputStream(patchFile))).toList

  def createCompleteBinaryImage(patchImageInfo: List[(BufferedImage, InferenceInfo)],
                                destinationImage: BufferedImage,
                                destinationFile: File): Boolean = {
    patchImageInfo.foreach { case (image, info) =>
        for (y <- info.yMin to info.yMax;
              x <- info.xMin to info.xMax)
          writePixel(x, y, image.getRGB(x,y), destinationImage§)
    }
    if(!patchImageInfo.isEmpty) ImageIO.write(destinationImage, "png", destinationFile) else false
  }

  def writePixel(xCoordinate:Int, yCoordinate:Int, rgb:Int, destination: BufferedImage):Unit = {
    if (new RGBPixel(rgb) != AppConstants.VoidRGB)
      destination.setRGB(xCoordinate, yCoordinate, rgb)
  }
}
