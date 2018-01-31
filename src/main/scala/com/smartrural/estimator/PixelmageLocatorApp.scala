package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.AppModule
import com.smartrural.estimator.runner.PixelLocatorRunner
import com.smartrural.estimator.util.AppConstants
import org.slf4j.LoggerFactory

object PixelmageLocatorApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.PixelmageLocatorApp <Conf_path>'")
      System.exit(1)
    }

    implicit val imageReconstructionService = new AppModule

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    val originalImagesPath = properties.getProperty(AppConstants.PropertyOriginalImagePath)
    val binaryImagesPath = properties.getProperty(AppConstants.BinaryImagesPath)
    val destinationPath = properties.getProperty(AppConstants.DestinationPath)
    val radius = properties.getProperty(AppConstants.RadiusPixelLocator)

    new PixelLocatorRunner(originalImagesPath, binaryImagesPath, destinationPath, radius.toInt).run
  }
}
