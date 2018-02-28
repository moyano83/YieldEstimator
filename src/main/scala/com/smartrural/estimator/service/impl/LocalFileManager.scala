package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.service.FileManagerService
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.IOFileFilter
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

/**
  * Created by jm186111 on 29/01/2018.
  */
class LocalFileManager extends FileManagerService{

  private val logger = LoggerFactory.getLogger(getClass)

  override def getChildList(path:String, filter: IOFileFilter, dirFilter: IOFileFilter):Array[File] =
    FileUtils.listFiles(new File(path), filter, dirFilter).toArray[File](Array()).sortBy(_.getName)

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

  override def writeObjAsLineToFile(obj:Any, file:File):Boolean =
    Try(FileUtils.writeLines(file, List(obj.toString), true)) match {
      case Success(_) => true
      case Failure(e) => {
        logger.error(s"Error writing obj=[${obj.toString}] to file=[${file.getAbsolutePath}]",e)
        false
      }
    }

}
