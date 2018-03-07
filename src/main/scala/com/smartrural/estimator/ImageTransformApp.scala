package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.YieldEstimatorModule
import com.smartrural.estimator.runner.ImageFilterRunner
import com.smartrural.estimator.service.FileManagerService
import com.smartrural.estimator.transformer.impl._
import com.smartrural.estimator.util.AppConstants._
import org.opencv.core.{Core, Mat}
import org.slf4j.LoggerFactory
import scaldi.{Injectable, Injector}

/**
  * Application which defines a pipeline that applies several filters and transformations to the original images
  * contained in a specified path in order to filter out non leaf pixels in the pictures
  */
object ImageTransformApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.ImageTransformApp <Conf_path>'")
      System.exit(1)
    }
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    val originalImagesPath = properties.getProperty(PropertyOriginalImagePath)
    val bboxesPath = properties.getProperty(PropertyBBoxesPath)
    val destinationPath = properties.getProperty(PropertyDestinationPath)
    val radius = properties.getProperty(PropertyRadiusPixelLocator).toInt
    val sigmaValue = Option(properties.getProperty(PropertyGaussSigmaValue)).map(_.toInt).getOrElse(1)
    val sampleImageHistogramLocation = properties.getProperty(PropertySampleImageHistogram)


    if (Some(bboxesPath).isEmpty ||
    Some(originalImagesPath).isEmpty ||
    Some(destinationPath).isEmpty ||
    Some(radius).isEmpty ||
    Some(sigmaValue).isEmpty ||
    Some(sampleImageHistogramLocation).isEmpty ){

      logger.error("Invalid set of parameters to run the Image transform process. Please review the configuration")
      System.exit(1)
    }

    implicit val appModule = new YieldEstimatorModule
    val sampleImageMat = new FileManagerHelper().readImage(new File(sampleImageHistogramLocation))

    val listFilters = List(
      new MedianFilterTransformer(radius),
      new GaussianFilterTransformer(radius, sigmaValue),
      new HistogramFilterTransformer(radius, sampleImageMat)
    )

    val runner = new ImageFilterRunner(bboxesPath, originalImagesPath, destinationPath, listFilters)
    val appThread = new Thread(runner)
    appThread.start()
  }

  class FileManagerHelper(implicit val inj:Injector) extends Injectable{
    val fileManagerService = inject[FileManagerService]
    def readImage(file:File):Mat = fileManagerService.readImage(file)
  }
}
