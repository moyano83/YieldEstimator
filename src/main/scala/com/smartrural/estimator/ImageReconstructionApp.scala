package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.AppModule
import com.smartrural.estimator.runner.ImageReconstructionRunner
import com.smartrural.estimator.util.AppConstants
import org.slf4j.LoggerFactory

object ImageReconstructionApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.ImageReconstructionApp <Conf_path>'")
      System.exit(1)
    }

    implicit val imageReconstructionService = new AppModule

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    val bboxesPath = properties.getProperty(AppConstants.PropertyBBoxesPath)
    val originalImagesPath = properties.getProperty(AppConstants.PropertyOriginalImagePath)
    val patchImgPath = properties.getProperty(AppConstants.PropertyPatchesPath)
    val destinationPath = properties.getProperty(AppConstants.DestinationPath)

    new ImageReconstructionRunner(bboxesPath, originalImagesPath, patchImgPath, destinationPath).run
  }
}
