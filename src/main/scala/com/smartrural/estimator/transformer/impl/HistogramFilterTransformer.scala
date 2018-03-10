package com.smartrural.estimator.transformer.impl

import java.io.File

import com.smartrural.estimator.transformer.ImageTransformer
import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core._
import org.opencv.imgproc.Imgproc

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

/**
  * Histogram backprojection filter transformer
  * @param radius the radius of the transformation
  * @param sampleImage the image containing the object to search for
  */
class HistogramFilterTransformer(val radius:Int, val sampleImage:Mat) extends ImageTransformer {

  // size of the h, s and v bins
  val histSize = new MatOfInt(255, 255, 255);
  // hue varies from 0 to 180, saturation and value from 0 to 256
  val ranges =  new MatOfFloat(0f,180f,0f,256f,0f,256f);
  // we compute the histogram from the 0-th and 2-st channels
  val channels = new MatOfInt(0, 1, 2);

  val displayImageSideSize = 512

  val cvtRoi = getCVT(sampleImage)

  val hsvRoi = splitPlanes(cvtRoi)

  val sampleHist = histogramHSV(hsvRoi)
  /**
    * Calculates the histogram for the given image
    * @param hsvPlanes the hsv planes
    * @return the histogram image
    */
  def histogramHSV(hsvPlanes:ArrayBuffer[Mat]): Mat = {
    val histRef = getMat()
    Imgproc.calcHist(hsvPlanes, channels, getMat(), histRef, histSize, ranges)
    Core.normalize(histRef, histRef, 0, displayImageSideSize, Core.NORM_MINMAX)
    histRef
  }

  /**
    * @inheritdoc
    */
  override def transform(matFile:File, originalImg:Mat):Mat = {
    val originalImgHSV = getCVT(originalImg)
    val hsvt = splitPlanes(originalImgHSV)

    val dst = getMat()
    Imgproc.calcBackProject(hsvt, channels, sampleHist, dst, ranges, 1)

    // Now convolute with circular disc
    val disc = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(radius,radius))

    Imgproc.filter2D(dst, dst, -1, disc)

    // threshold and binary AND
    val thresh, res = getMat()
    Imgproc.threshold(dst, thresh, 100, 255, Imgproc.THRESH_BINARY)
    Core.merge(ArrayBuffer(thresh,thresh,thresh), thresh)

    originalImgHSV.copyTo(res, thresh)

    getRGB(res)
  }



}
