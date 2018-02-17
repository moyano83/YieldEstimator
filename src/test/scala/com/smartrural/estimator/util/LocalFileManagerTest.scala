package com.smartrural.estimator.util

import java.io.File

import com.smartrural.estimator.service.impl.LocalFileManager
import org.junit.runner.RunWith
import org.opencv.core.Core
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class LocalFileManagerTest extends FlatSpec{

  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

  val rootPath = getClass.getClassLoader.getResource(".").getPath
  val originalImagesPath = new File(rootPath, "original_images").getPath
  val inferencesPath = new File(rootPath, "inferences").getAbsolutePath
  val partition = "valdemonjas-2017-09-13_01"
  val imageName = "z-img-000-000004.jpg"

  val fileManagerService = new LocalFileManager

  behavior of "LocalFileManager"

  it should "compose the File from a list of relative paths" in {
    val filePaths = List("usr","local","bin","bash")
    assert(fileManagerService.getComposedFile(filePaths).getPath == "usr/local/bin/bash")
    val filePaths2 = List("/usr","local","bin","bash")
    assert(fileManagerService.getComposedFile(filePaths2).getPath == "/usr/local/bin/bash")
  }

  it should "List all the child files" in {
    val inferencesFolder = fileManagerService.getChildList(inferencesPath)
    assert(inferencesFolder.size == 1)
    assert(inferencesFolder.head.getName == partition)

    val picturesList = fileManagerService.getChildList(inferencesFolder.head.getPath)
    assert(picturesList.size == 7)
  }

  it should "find the mirror image" in{
    val imageToMirror = new File(originalImagesPath, s"$partition/$imageName")
    assert(fileManagerService.getMirrorImageFile(imageToMirror, s"$rootPath/test").getAbsolutePath == rootPath.concat
    (s"test/$partition/$imageName"))
  }

  it should "fail to retrieve the mirror image if there is no partitions" in {
    val imageToMirror = new File(s"/$imageName")
    assert(fileManagerService.getMirrorImageFile(imageToMirror, s"${rootPath}/test").getAbsolutePath ==
      s"${rootPath}test/$imageName")
  }
}
