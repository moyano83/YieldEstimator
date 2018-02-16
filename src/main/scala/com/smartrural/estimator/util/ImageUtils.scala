package com.smartrural.estimator.util

import java.awt.image.{BufferedImage, DataBufferByte, DataBufferInt}
import java.lang.Math.{max, min}

import com.smartrural.estimator.model.ColoredPixel
import com.smartrural.estimator.util.AppConstants.ZeroCoordinate
import org.opencv.core.{Core, CvType, Mat}
import org.opencv.imgproc.Imgproc

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by jm186111 on 14/02/2018.
  */
object ImageUtils {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  /**
    * gets the colored pixel from the mat corresponding to the coordinates passed
    * @param mat the image
    * @param x row coordinate
    * @param y column coordinate
    * @return the Colored pixel
    */
  def arrayToColoredPixel(mat: Mat, x:Int, y:Int):ColoredPixel = {
    mat.get(x,y) match {
      case Array(r:Double, g:Double, b:Double) => new ColoredPixel(r.toInt,g.toInt,b.toInt,x,y)
      case value => throw new Exception(s"Couldn't parse value=[$value]")
    }
  }

  def extractSurroundingPixels(img:Mat, radius:Int, pixel:ColoredPixel):List[ColoredPixel] =
    (for { x <- max(pixel.x - radius, ZeroCoordinate) to min(pixel.x + radius, img.width() - 1);
           y <- max(pixel.y - radius, ZeroCoordinate) to min(pixel.y + radius, img.height() - 1)
    } yield arrayToColoredPixel(img,x,y)).toList

  /**
    * Converts the Mat into an Image
    * @param m the mat
    * @return the image
    */
  def toBufferedImage(m: Mat, imageType:Option[Int]):BufferedImage= {
    val typeChannel = imageType.getOrElse(
      if (m.channels() > 1) BufferedImage.TYPE_3BYTE_BGR
      else BufferedImage.TYPE_BYTE_GRAY
    )
    val bufferSize = m.channels()*m.cols()*m.rows()
    val b = Array.ofDim[Byte](bufferSize)
    m.get(0,0,b)
    val image = new BufferedImage(m.cols(),m.rows(), typeChannel)
    val targetPixels = image.getRaster().getDataBuffer().asInstanceOf[DataBufferByte].getData()
    System.arraycopy(b, 0, targetPixels, 0, b.length)
    image
  }

  /**
    * Converts the Buffered Image to a Mat
    * @param bi the buffered image
    * @return the Mat
    */
  def toMat(bi:BufferedImage):Mat = {
    val mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3)
    val data = bi.getRaster().getDataBuffer().asInstanceOf[DataBufferByte].getData()
    mat.put(0, 0, data)
    mat
  }

  /**
    * Wrapper for the cvtColor method
    * @param image the image
    * @return the cvtColor
    */
  def getCVT(image:Mat):Mat = {
    val dstImg = new Mat()
    Imgproc.cvtColor(image, dstImg, Imgproc.COLOR_BGR2HSV)
    dstImg
  }

  /**
    * Wrapper for the cvtColor method
    * @param image the image
    * @return the cvtColor
    */
  def getRGB(image:Mat):Mat = {
    val dstImg = new Mat()
    Imgproc.cvtColor(image, dstImg, Imgproc.COLOR_HSV2BGR)
    dstImg
  }

  /**
    * Split planes
    * @param image the image
    * @return the Planes
    */
  def splitPlanes(image: Mat):ArrayBuffer[Mat] = {
    val hsvPlanes = ArrayBuffer[Mat]()
    Core.split(image, hsvPlanes)
    hsvPlanes
  }

}
