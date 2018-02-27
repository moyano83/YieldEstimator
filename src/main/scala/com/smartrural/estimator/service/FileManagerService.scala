package com.smartrural.estimator.service

import java.io.File

import org.opencv.core.Mat

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
    * @param output     a <code>File</code> to be written to.
    * @return <code>false</code> if no appropriate writer is found.
    */
  def writeImage(im: Mat, output: File): Boolean

  /**
    * Wrapper of the ImageIO static read function, so it is possible to mock this call
    *
    * @param input the file to read from
    * @return the mat
    */
  def readImage(input: File): Mat

  /**
    * writes the given object into the destination file
    * @param obj the obj to write as a line
    * @param file the destination file
    */
  def writeObjAsLineToFile(obj:Any, file:File):Boolean
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

}
