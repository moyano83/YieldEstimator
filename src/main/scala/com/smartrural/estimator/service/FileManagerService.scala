package com.smartrural.estimator.service

import java.awt.image.{BufferedImage, RenderedImage}
import java.io.{File, InputStream}

import com.smartrural.estimator.model.ColoredPixel

/**
  * Created by jm186111 on 01/02/2018.
  */
trait FileManagerService {
  /**
    * Returns the list of files contained in the folder passed
    * @param path the path to the folder
    * @return the list of child files
    */
  def getChildList(path: String): Array[File]

  /**
    * Gets a file that is mirroring the passed one with a different root path
    * @param imageToMirror the image to mirror
    * @param mirrorBasePath the base path to construct the mirror image
    * @return the File representing the mirror file
    */
  def getMirrorImageFile(imageToMirror: File, mirrorBasePath: String): File

  /**
    * Wrapper of the ImageIO static write function, so it is possible to mock this call
    *
    * @param im         a <code>RenderedImage</code> to be written.
    * @param formatName a <code>String</code> containing the informal
    *                   name of the format.
    * @param output     a <code>File</code> to be written to.
    * @return <code>false</code> if no appropriate writer is found.
    */
  def writeImage(im: RenderedImage, formatName: String, output: File): Boolean

  /**
    * Wrapper of the ImageIO static read function, so it is possible to mock this call
    *
    * @param input an <code>InputStream</code> to read from.
    * @return a <code>BufferedImage</code> containing the decoded
    *         contents of the input, or <code>null</code>.
    */
  def readImage(input: InputStream): BufferedImage

  /**
    * Wrapper of the ImageIO static read function, so it is possible to mock this call
    *
    * @param input the file to read from
    * @return a <code>BufferedImage</code> containing the decoded
    *         contents of the input, or <code>null</code>.
    */
  def readImage(input: File): BufferedImage

  /**
    * Gets a composition of the relative paths into a File
    * @param relativePathLists the paths list
    * @return the file composed
    */
  def getComposedFile(relativePathLists:List[String]):File = {
    val reversedRelativePathLists = relativePathLists.reverse
    def getComposedFileAux(relativePaths:List[String]):File = relativePaths match{
      case filePath :: Nil => new File(filePath)
      case filePath :: elementList => new File(getComposedFileAux(elementList), filePath)
    }
    getComposedFileAux(reversedRelativePathLists)
  }

  /**
    * Writes the given pixel in the image passed
    * @param pixel the pixel value
    * @param destination the destination image
    */
  def writePixel(pixel:ColoredPixel, destination: BufferedImage):Unit

  /**
    * Gets the formated resolution for the sizes passed
    * @param img buffered Image
    * @return the resolution in a formatted string
    */
  def getFormattedResolution(img:BufferedImage) = s"${img.getWidth} x ${img.getHeight}"
}
