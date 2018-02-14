package com.smartrural.estimator.transformer

import java.awt.Image
import java.awt.image.{BufferedImage, DataBufferByte}

import org.opencv.core.{Core, CvType, Mat, MatOfInt}
import org.opencv.imgproc.Imgproc

import scala.collection.JavaConversions._
/**
  * Created by jm186111 on 13/02/2018.
  */
class HistogramFilterTransformer(val numberOfBins: Int = 256)
                          extends ImageTransformer {

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  /**
    * Transform an imagen according to the internal implementation of the Transformer
    *
    * @param img the image to transform
    * @return the transformed image
    */
  override def applyTransform(img: BufferedImage): BufferedImage = {
    val matImage = toMat(img)
    val hsv = new Mat()
    Imgproc.cvtColor(matImage, hsv, Imgproc.COLOR_BGR2HSV)
    val hue = new Mat()
    hue.create(hsv.size(), hsv.depth())
    val ch = new MatOfInt(0, 0);
    Core.mixChannels(List(hsv), List(hue), ch);
    img
  }

  /**
    * Converts the Mat into an Image
    * @param m the mat
    * @return the image
    */
  def toBufferedImage(m: Mat):Image= {
    val typeChannel = if (m.channels() > 1) BufferedImage.TYPE_3BYTE_BGR else BufferedImage.TYPE_BYTE_GRAY
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
}
