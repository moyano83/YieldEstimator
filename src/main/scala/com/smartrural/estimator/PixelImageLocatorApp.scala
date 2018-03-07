package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties
import com.smartrural.estimator.di.YieldEstimatorModule
import com.smartrural.estimator.runner.PixelLocatorRunner
import com.smartrural.estimator.util.AppConstants._
import org.opencv.core.Core
import org.slf4j.LoggerFactory

/**
  * Application that extracts the cluster surrounding pixels within a given radius, and then matches the position of
  * those pixels to the output images coming from the image processing application
  */
object PixelImageLocatorApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    // Validation of the input parameters
    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.PixelmageLocatorApp <Conf_path>'")
      System.exit(1)
    }

    // Explicit loading if the OpenCV library is needed to make use of it after
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    // Loading the properties from the configuration file
    val inferencesFile = new File(properties.getProperty(PropertyInferencesFile))
    val transformedImgPath = properties.getProperty(PropertyFilteredImagePath)
    val reconstructedImgPath = properties.getProperty(PropertyReconstructedImagePath)
    val dstFile = new File(properties.getProperty(PropertyDestinationPath))
    val radius = properties.getProperty(PropertyRadiusPixelLocator).toInt

    // Validating the expected properties are defined
    if (Some(inferencesFile).isEmpty || Some(transformedImgPath).isEmpty || Some(reconstructedImgPath).isEmpty ||
      Some(dstFile).isEmpty || Some(radius).isEmpty ){
        logger.error("Invalid set of parameters to run the Pixel locator process. Please review the configuration")
        System.exit(1)
    }

    // Instantiation of the corresponding services
    implicit val module = new YieldEstimatorModule

    // Creating the Runnable to execute the application logic
    val runner = new PixelLocatorRunner(inferencesFile, transformedImgPath, reconstructedImgPath, dstFile, radius)
    //new Thread(runner).start()
    runner.run()
  }
}
