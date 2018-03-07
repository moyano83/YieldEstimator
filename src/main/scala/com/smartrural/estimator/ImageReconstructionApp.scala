package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.YieldEstimatorModule
import com.smartrural.estimator.runner.ImageReconstructionRunner
import com.smartrural.estimator.util.AppConstants
import org.opencv.core.Core
import org.slf4j.LoggerFactory

/**
  * Application that reconstructs a binary image (only black and red pixels) from the partial inference pictures and
  * the inference information available
  */
  object ImageReconstructionApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.ImageReconstructionApp <Conf_path>'")
      System.exit(1)
    }
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    implicit val imageReconstructionService = new YieldEstimatorModule

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    val bboxesPath = properties.getProperty(AppConstants.PropertyBBoxesPath)
    val originalImagesPath = properties.getProperty(AppConstants.PropertyOriginalImagePath)
    val patchImgPath = properties.getProperty(AppConstants.PropertyPatchesPath)
    val destinationPath = properties.getProperty(AppConstants.PropertyReconstructedImagesPath)

    if (Some(bboxesPath).isEmpty ||
      Some(originalImagesPath).isEmpty ||
      Some(patchImgPath).isEmpty ||
      Some(destinationPath).isEmpty ){

      logger.error("Invalid set of parameters to run the Image reconstruction process. Please review the configuration")
      System.exit(1)
    }

    val runner = new ImageReconstructionRunner(bboxesPath, originalImagesPath, patchImgPath, destinationPath)

    val appThread = new Thread(runner)
    appThread.start()
  }
}
