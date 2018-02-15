package com.smartrural.estimator.util

import java.awt.image.{BufferedImage, DataBufferByte}
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

  def extractSurroundingPixels(img:BufferedImage, radius:Int, pixel:ColoredPixel):List[ColoredPixel] =
    (for { x <- max(pixel.x - radius, ZeroCoordinate) to min(pixel.x + radius, img.getWidth - 1);
           y <- max(pixel.y - radius, ZeroCoordinate) to min(pixel.y + radius, img.getHeight - 1)
    } yield new ColoredPixel(img, x, y)).toList

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
    * Split planes
    * @param image the image
    * @return the Planes
    */
  def splitPlanes(image: Mat):ArrayBuffer[Mat] = {
    val hsvPlanes = ArrayBuffer[Mat]()
    Core.split(image, hsvPlanes)
    hsvPlanes
  }

  /**
    * Gets a void image with the same size than the original one
    * @param img the image to base the copy to
    * @param isCopy returns a copy if the image if this is true, otherwise returns a blank canvas
    * @return the bufferedImage
    */
  def getImageCanvas(img:BufferedImage, isCopy:Boolean):BufferedImage =
    if(isCopy){
      val cm = img.getColorModel()
      val isAlphaPremultiplied = cm.isAlphaPremultiplied()
      val raster = img.copyData(null)
      new BufferedImage(cm, raster, isAlphaPremultiplied, null)
    }else{
      new BufferedImage(img.getWidth, img.getHeight, BufferedImage.TYPE_INT_RGB)
    }
}
