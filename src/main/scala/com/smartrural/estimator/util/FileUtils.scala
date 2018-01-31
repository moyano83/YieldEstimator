package com.smartrural.estimator.util

import java.io.File

import org.apache.commons.io.filefilter.WildcardFileFilter

/**
  * Created by jm186111 on 29/01/2018.
  */
object FileUtils {
  def getChildList(path:String):Array[File] =
    new File(path)
      .list(new WildcardFileFilter("*"))
      .map(file=>new File(path, file))
      .sortBy(_.getName)
}
