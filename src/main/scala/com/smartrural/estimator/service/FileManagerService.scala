package com.smartrural.estimator.service

import java.io.File

import org.apache.commons.io.filefilter.IOFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter.{INSTANCE => IOfilter}
import org.opencv.core.Mat

/**
  * Service to deal with file IO and path management and
  */
trait FileManagerService {

  private val filter = new IOFilterDSStore(IOfilter)
  /**
    * Returns the list of files contained in the folder passed
    * @param path the path to the folder
    * @param filter the filename filter
    * @param dirFilter the dir filename filter
    * @return the list of child files
    */
  def getChildList(path: String, filter:IOFileFilter = filter, dirFilter:IOFileFilter = filter): Array[File]
  /**
    * Gets a file that is mirroring the passed one with a different root path
    * @param imageToMirror the image to mirror
    * @param mirrorBasePath the base path to construct the mirror image
    * @return the File representing the mirror file
    */
  def getMirrorFile(imageToMirror: File, mirrorBasePath: String): File

  /**
    * Gets a file that is mirroring the passed one with a different root path
    * @param imageToMirror the image to mirror
    * @param mirrorBasePath the base path to construct the mirror image
    * @param extension the extension that the image should have
    * @return the File representing the mirror file
    */
  def getMirrorFileWithExtension(imageToMirror: File, mirrorBasePath: String, extension:String): File
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
      case _ => throw new IllegalArgumentException("Unrecognized collection")
    }
    getComposedFileAux(reversedRelativePathLists)
  }

  private [FileManagerService] class IOFilterDSStore(filter:IOFileFilter) extends IOFileFilter {
    override def accept(file: File): Boolean = if (file.getName == ".DS_Store") false else filter.accept(file)

    override def accept(dir: File, name: String): Boolean =
      if (name == ".DS_Store") false
      else filter.accept(dir, name)
  }
}
