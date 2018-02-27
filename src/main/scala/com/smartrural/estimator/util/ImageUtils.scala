package com.smartrural.estimator.util

import java.lang.Math.{max, min}

import com.smartrural.estimator.model.{ColoredPixel, BBoxItemInfo}
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
    * @param row row coordinate
    * @param col column coordinate
    * @return the Colored pixel
    */
  def arrayToColoredPixel(mat: Mat, row:Int, col:Int):ColoredPixel = {
    mat.get(row,col) match {
      case Array(r:Double, g:Double, b:Double) => new ColoredPixel(r.toInt, g.toInt, b.toInt, row, col)
      case value => throw new Exception(s"Couldn't parse value=[$value]")
    }
  }

  /**
    * Gets the list of the surrounding pixels by radius
    * @param img the image
    * @param radius the radiuls
    * @return the list of surrounding pixels
    */
  def extractSurroundingPixels(img:Mat, radius:Int):Set[ColoredPixel] =
    (for {row <- ZeroCoordinate to img.rows();
                          col <- ZeroCoordinate to img.cols();
                          pixel = arrayToColoredPixel(img, row, col) if pixel != AppConstants.VoidColor
    } yield extractSurroundingPixels(img, radius, pixel)).flatten.toSet

  /**
    * Gets the list of the surrounding pixels by radius
    * @param img the image
    * @param radius the radiuls
    * @param pixel the central pixel
    * @return the list of surrounding pixels
    */
  def extractSurroundingPixels(img:Mat, radius:Int, pixel:ColoredPixel):Set[ColoredPixel] =
    (for { x <- max(pixel.row - radius, ZeroCoordinate) to min(pixel.row + radius, img.width() - 1);
           y <- max(pixel.col - radius, ZeroCoordinate) to min(pixel.col + radius, img.height() - 1)
    } yield arrayToColoredPixel(img,x,y)).toSet

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
  def getMat(rows:Int, cols:Int) = {
    val mat = new Mat(rows, cols, CvType.CV_8UC3)
    for(x<-0 until rows;
        y<-0 until cols){
      mat.put(x,y,AppConstants.VoidColor)
    }
    mat
  }

  /**
    * returns an empty Mat
    * @return the mat
    */
  def getMat() = new Mat()
}
