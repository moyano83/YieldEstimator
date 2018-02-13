package com.smartrural.estimator.service.impl

import java.awt.image.{BufferedImage, RenderedImage}
import java.io.{File, InputStream}
import javax.imageio.ImageIO

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.service.FileManagerService
import org.apache.commons.io.filefilter.WildcardFileFilter

/**
  * Created by jm186111 on 29/01/2018.
  */
class LocalFileManager extends FileManagerService{
  override def getChildList(path:String):Array[File] =
    new File(path)
      .list(new WildcardFileFilter("*"))
      .map(file=>new File(path, file))
      .sortBy(_.getName)

  override def getMirrorImageFile(imageToMirror:File, mirrorBasePath:String):File = {
    val imageName = imageToMirror.getName
    val partitionFolder = imageToMirror.getParentFile.getName
    new File(new File(mirrorBasePath, partitionFolder), imageName)
  }

  override def writeImage(im: RenderedImage, format: String, output: File): Boolean = ImageIO.write(im, format, output)

  override def readImage(input: InputStream): BufferedImage = ImageIO.read(input)

  override def readImage(input: File): BufferedImage = ImageIO.read(input)

  override def writePixel(pixel: ColoredPixel, destination: BufferedImage) =
    if (!pixel.isVoid()) destination.setRGB(pixel.x, pixel.y, pixel.rgbColor)
}
