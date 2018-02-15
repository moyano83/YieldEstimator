package com.smartrural.estimator.transformer

import com.smartrural.estimator.util.ImageUtils._
import org.opencv.core._
import org.opencv.imgproc.Imgproc

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
/**
  * Created by jm186111 on 13/02/2018.
  */
class HistogramFilterTransformer(val sampleImage:Mat) extends ImageTransformer {

  val hBins = 32
  val sBins = 32
  val vBins = 32

  val hRange = new MatOfFloat(0f, 180f)
  val sRange = new MatOfFloat(0f, 255f)
  val vRange = new MatOfFloat(0f, 255f)
  val displayImageSideSize = 512

  val cvtRoi = getCVT(sampleImage)

  val hsvRoi = splitPlanes(cvtRoi)
  /**
    * Calculates the histogram for the given image
    * @param hsvPlanes the hsv planes
    * @return the histogram image
    */
  def histogramHSV(hsvPlanes:ArrayBuffer[Mat]): Mat = {
    val hMat, sMat, vMat = new Mat()

    Imgproc.calcHist(List(hsvPlanes(0)), new MatOfInt(0), new Mat(), hMat, new MatOfInt(hBins), hRange)
    Imgproc.calcHist(List(hsvPlanes(1)), new MatOfInt(0), new Mat(), sMat, new MatOfInt(sBins), sRange)
    Imgproc.calcHist(List(hsvPlanes(2)), new MatOfInt(0), new Mat(), vMat, new MatOfInt(vBins), vRange)

    val finalHistImage = new Mat(displayImageSideSize, displayImageSideSize, CvType.CV_8UC1)

    Core.normalize(hMat, finalHistImage, 0, displayImageSideSize, Core.NORM_MINMAX)
    Core.normalize(sMat, finalHistImage, 0, displayImageSideSize, Core.NORM_MINMAX)
    Core.normalize(vMat, finalHistImage, 0, displayImageSideSize, Core.NORM_MINMAX)

    finalHistImage
  }

  /**
    * Transform an imagen according to the internal implementation of the Transformer
    *
    * @param originalImg the image to transform
    * @return the transformed image
    */
  override def applyTransform(originalImg: Mat): Mat = {
    val backProjection = histogramHSV(splitPlanes(getCVT(originalImg)))
    backProjection
  }



}
