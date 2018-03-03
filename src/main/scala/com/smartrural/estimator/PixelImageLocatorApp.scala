package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.YieldEstimatorModule
import com.smartrural.estimator.runner.PixelLocatorRunner
import com.smartrural.estimator.util.AppConstants._
import org.opencv.core.Core
import org.slf4j.LoggerFactory

object PixelImageLocatorApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.PixelmageLocatorApp <Conf_path>'")
      System.exit(1)
    }

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    val inferencesFile = new File(properties.getProperty(PropertyInferencesFile))
    val transformedImagePath = properties.getProperty(PropertyFilteredImagePath)
    val reconstructedImagePath = properties.getProperty(PropertyReconstructedImagePath)
    val destinationFile = new File(properties.getProperty(PropertyDestinationPath))
    val radius = properties.getProperty(PropertyRadiusPixelLocator).toInt

    implicit val module = new YieldEstimatorModule

    if (Some(inferencesFile).isEmpty ||
      Some(transformedImagePath).isEmpty ||
      Some(reconstructedImagePath).isEmpty ||
      Some(destinationFile).isEmpty ||
      Some(radius).isEmpty ){

      logger.error("Invalid set of parameters to run the Pixel locator process. Please review the configuration")
      System.exit(1)
    }

    new PixelLocatorRunner(inferencesFile, transformedImagePath, reconstructedImagePath, destinationFile, radius).run
  }


}
