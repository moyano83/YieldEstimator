package com.smartrural.estimator.util

import java.lang.Math.{max, min}

import com.smartrural.estimator.model.{ColoredPixel, InferenceInfo}
import com.smartrural.estimator.util.AppConstants.ZeroCoordinate
import org.opencv.core.{Core, CvType, Mat}
import org.opencv.imgproc.Imgproc

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by jm186111 on 14/02/2018.
  */
object ImageUtils {

  /**
    * Gets the formated resolution for the sizes passed
    * @param rows number of rows
    * @param cols number of columns
    * @return the resolution in a formatted string
    */
  def getFormattedResolution(rows:Int, cols:Int) = s"${rows} x ${cols}"

  /**
    * gets the colored pixel from the mat corresponding to the coordinates passed
    * @param mat the image
    * @param x row coordinate
    * @param y column coordinate
    * @return the Colored pixel
    */
  def arrayToColoredPixel(mat: Mat, x:Int, y:Int):ColoredPixel = {
    mat.get(x,y) match {
      case Array(r:Double, g:Double, b:Double) => new ColoredPixel(r.toInt, g.toInt, b.toInt, x, y)
      case value => throw new Exception(s"Couldn't parse value=[$value]")
    }
  }

  def extractSurroundingPixels(img:Mat, radius:Int, pixel:ColoredPixel):List[ColoredPixel] =
    (for { x <- max(pixel.row - radius, ZeroCoordinate) to min(pixel.row + radius, img.width() - 1);
           y <- max(pixel.col - radius, ZeroCoordinate) to min(pixel.col + radius, img.height() - 1)
    } yield arrayToColoredPixel(img,x,y)).toList

  /**
    * Wrapper for the cvtColor method
    * @param image the image
    * @return the cvtColor
    */
  def getCVT(image:Mat):Mat = {
    val dstImg = getMat()
    Imgproc.cvtColor(image, dstImg, Imgproc.COLOR_BGR2HSV)
    dstImg
  }

  /**
    * Wrapper for the cvtColor method
    * @param image the image
    * @return the cvtColor
    */
  def getRGB(image:Mat):Mat = {
    val dstImg = getMat()
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

  /**
    * Gets the mat as a list of colored pixels
    * @param img the mat
    * @return the list of colored pixels
    */
  def getMatAsColoredPixels(img:Mat):List[ColoredPixel] =
    (for (y <- 0 until img.width();
         x <- 0 until img.height()
    ) yield ImageUtils.arrayToColoredPixel(img, x, y)).toList

  /**
    * Gets a new mat
    * @param rows the rows
    * @param cols the columns
    * @return the new Mat
    */
  def getMat(rows:Int, cols:Int) = new Mat(rows, cols, CvType.CV_8UC3)

  /**
    * returns an empty Mat
    * @return the mat
    */
  def getMat() = new Mat()
}
