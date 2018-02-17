package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.service.FileManagerService
import org.apache.commons.io.filefilter.WildcardFileFilter
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs

/**
  * Created by jm186111 on 29/01/2018.
  */
class LocalFileManager extends FileManagerService{

  override def getChildList(path:String):Array[File] =
    new File(path).list(new WildcardFileFilter("*")).map(file=>new File(path, file)).sortBy(_.getName)

  override def getMirrorImageFile(imageToMirror:File, mirrorBasePath:String):File = {
    val imageName = imageToMirror.getName
    val partitionFolder = imageToMirror.getParentFile.getName
    new File(new File(mirrorBasePath, partitionFolder), imageName)
  }

  override def writeImage(im: Mat, output: File): Boolean = {
    output.getParentFile.mkdirs()
    Imgcodecs.imwrite(output.getAbsolutePath, im)
  }

  override def readImage(input: File): Mat = Imgcodecs.imread(input.getAbsolutePath)

}
