package com.smartrural.estimator.util

import com.smartrural.estimator.model.BinaryPixel
import com.smartrural.estimator.util.AppConstants._
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
  def arrayToBinaryPixel(mat: Mat, row:Int, col:Int):BinaryPixel = {
    mat.get(row,col) match {
      case Array(r:Double, g:Double, b:Double) => new BinaryPixel((r.toInt + g.toInt + b.toInt) == 0, row, col)
      case value => throw new Exception(s"Couldn't parse value=[$value]")
    }
  }
  def getListPositionByCoordinate(maxCols:Int, row:Int, col:Int):Int = (row + col * maxCols)

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
    ) yield ImageUtils.arrayToBinaryPixel(img, row, col)).toList


  /**
    * Gets the mat as a list of colored pixels
    * @param img the mat
    * @return the list of colored pixels
    */
  def getNonVoidPixelCount(img:Mat):Double =
    (for (row <- 0 until img.rows();
          col <- 0 until img.cols();
      px = if(ImageUtils.arrayToBinaryPixel(img, row, col).isNotVoid) 1 else 0
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
    * Gets a new mat
    * @param img to copy
    * @return the new Mat
    */
  def getCopyMat(img:Mat):Mat = {
    val mat = new Mat(img.rows, img.cols, CvType.CV_8UC3)
    for(row<-0 until img.rows;
        col<-0 until img.cols;
      pixel = img.get(row, col) match {
        case Array(0d, 0d, 0d) => VoidColor
        case _ => WhiteColor
      }) yield mat.put(row, col, pixel)
    mat
  }
  /**
    * returns an empty Mat
    * @return the mat
    */
  def getMat() = new Mat()

  /**
    * Sets the color of the pixel to red if it is not void
    * @param pixel the pixel to inspect
    * @return an array representing either void or red color
    */
  def getColorForPixel(pixel:BinaryPixel):Array[Byte] = if (pixel.isVoid) VoidColor else WhiteColor
}
