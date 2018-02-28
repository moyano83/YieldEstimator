package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.ImageReconstructionModule
import com.smartrural.estimator.runner.ImageReconstructionRunner
import com.smartrural.estimator.util.AppConstants
import org.opencv.core.Core
import org.slf4j.LoggerFactory

object ImageReconstructionApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.ImageReconstructionApp <Conf_path>'")
      System.exit(1)
    }
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    implicit val imageReconstructionService = new ImageReconstructionModule

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    val bboxesPath = properties.getProperty(AppConstants.PropertyBBoxesPath)
    val originalImagesPath = properties.getProperty(AppConstants.PropertyOriginalImagePath)
    val patchImgPath = properties.getProperty(AppConstants.PropertyPatchesPath)
    val destinationPath = properties.getProperty(AppConstants.PropertyDestinationPath)

    new ImageReconstructionRunner(bboxesPath, originalImagesPath, patchImgPath, destinationPath).run
  }
}
