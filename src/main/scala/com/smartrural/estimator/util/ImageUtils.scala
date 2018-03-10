package com.smartrural.estimator.util

import java.lang.Math.{max, min}

import com.smartrural.estimator.model.{BBoxItemInfo, BinaryPixel}
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
  def arrayToColoredPixel(mat: Mat, row:Int, col:Int):BinaryPixel = {
    mat.get(row,col) match {
      case Array(r:Double, g:Double, b:Double) => new BinaryPixel((r.toInt + g.toInt + b.toInt) == 0, row, col)
      case value => throw new Exception(s"Couldn't parse value=[$value]")
    }
  }

  def getMatAsListOfColouredPixels(img:Mat):List[BinaryPixel] =
    (for {row <- ZeroCoordinate until img.rows();
          col <- ZeroCoordinate until img.cols();
          pixel = arrayToColoredPixel(img, row, col)
  } yield pixel).toList

  def getListPositionByCoordinate(maxCols:Int, row:Int, col:Int):Int = (row + col * maxCols)
  /**
    * Gets the list of the surrounding pixels by radius
    * @param allPixels the list of pixels
    * @param radius the radiuls
    * @return the list of surrounding pixels
    */
  def extractAllSurroundingVoidPixelsFromImage(pixelsToInspect:List[BinaryPixel],
                                               allPixels:List[BinaryPixel],
                                               rows:Int,
                                               cols:Int,
                                               radius:Int):Set[BinaryPixel] = {
    pixelsToInspect
      .map(pixel => inspectSurroundingCoordinates(rows, cols, radius, pixel, allPixels))
      .flatten
      .toSet
  }

  /**
    * Gets the list of the surrounding pixels by radius not contained in the list passed
    * @param maxRows upper limit for rows
    * @param maxCols upper limit for cols
    * @param radius the radiuls
    * @param pixel the central pixel
    * @return the list of surrounding pixels
    */
  private def inspectSurroundingCoordinates(maxRows:Int,
                                            maxCols:Int,
                                            radius:Int,
                                            pixel:BinaryPixel,
                                            allPixels:List[BinaryPixel]):List[BinaryPixel] =
    (for {row <- max(pixel.row - radius, ZeroCoordinate) to min(pixel.row + radius, maxRows -1);
          col <- max(pixel.col - radius, ZeroCoordinate) to min(pixel.col + radius, maxCols -1);
          pixel = new BinaryPixel(true,row,col) if !allPixels.contains(pixel)
    } yield pixel).toList

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
  def getMatAsBinaryPixels(img:Mat):List[BinaryPixel] =
    (for (row <- 0 until img.rows();
         col <- 0 until img.cols()
    ) yield ImageUtils.arrayToColoredPixel(img, row, col)).toList


  /**
    * Gets the mat as a list of colored pixels
    * @param img the mat
    * @return the list of colored pixels
    */
  def getNonVoidPixelCount(img:Mat):Double =
    (for (row <- 0 until img.rows();
          col <- 0 until img.cols();
      px = if(ImageUtils.arrayToColoredPixel(img, row, col).isNotVoid) 1 else 0
    ) yield px).sum

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
