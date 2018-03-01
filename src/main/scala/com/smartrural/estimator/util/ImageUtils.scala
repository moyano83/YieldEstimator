package com.smartrural.estimator.util

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

  def getMatAsListOfColouredPixels(img:Mat):List[ColoredPixel] =
    (for {row <- ZeroCoordinate until img.rows();
          col <- ZeroCoordinate until img.cols();
          pixel = arrayToColoredPixel(img, row, col)
  } yield pixel).toList

  def getListPositionByCoordinate(maxCols:Int, row:Int, col:Int):Int = (row + col * maxCols)
  /**
    * Gets the list of the surrounding pixels by radius
    * @param img the image
    * @param radius the radiuls
    * @return the list of surrounding pixels
    */
  def extractAllSurroundingVoidPixelsFromImage(img:Mat, radius:Int):Set[ColoredPixel] = {
    val allPixels = getMatAsListOfColouredPixels(img)
    allPixels
      .filter(_.isNotVoid())
      .map(pixel => extractSurroundingCoordinates(img.rows, img.cols, radius, pixel, allPixels))
      .flatten
      .toSet
      .filter(_.isVoid())
  }

  /**
    * Gets the list of the surrounding pixels by radius
    * @param maxRows upper limit for rows
    * @param maxCols upper limit for cols
    * @param radius the radiuls
    * @param pixel the central pixel
    * @return the list of surrounding pixels
    */
  private def extractSurroundingCoordinates(maxRows:Int,
                                            maxCols:Int,
                                            radius:Int,
                                            pixel:ColoredPixel,
                                            allPixels:List[ColoredPixel]):List[ColoredPixel] =
    (for { row <- max(pixel.row - radius, ZeroCoordinate) to min(pixel.row + radius, maxRows -1);
           col <- max(pixel.col - radius, ZeroCoordinate) to min(pixel.col + radius, maxCols -1)
      position = getListPositionByCoordinate(maxCols, row, col)
    } yield allPixels(position)).toList

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
    (for (row <- 0 until img.rows();
         col <- 0 until img.cols()
    ) yield ImageUtils.arrayToColoredPixel(img, row, col)).toList

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
      mat.put(x, y, AppConstants.VoidColor)
    }
    mat
  }

  /**
    * returns an empty Mat
    * @return the mat
    */
  def getMat() = new Mat()
}
