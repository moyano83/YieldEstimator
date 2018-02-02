package com.smartrural.estimator.service

import java.io.File

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
}
