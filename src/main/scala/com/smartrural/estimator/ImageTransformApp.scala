package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.ImageReconstructionModule
import com.smartrural.estimator.runner.ImageFilterRunner
import com.smartrural.estimator.transformer.{AvgBlurFilterTransformer, GaussianFilterTransformer, HueFilterImageTransformer}
import com.smartrural.estimator.util.AppConstants._
import org.opencv.core.Core
import org.slf4j.LoggerFactory

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

    //Getting the color ranges, if not set, they will be unbounded
    val hueRange = Range(
      Option(properties.getProperty(PropertyHueMinValue)).map(_.toInt).getOrElse(0),
      Option(properties.getProperty(HueMaxValue)).map(_.toInt).getOrElse(360)
    )
    val saturationRange = Range(
      Option(properties.getProperty(PropertySaturationMinValue)).map(_.toInt).getOrElse(0),
      Option(properties.getProperty(PropertySaturationMaxValue)).map(_.toInt).getOrElse(100)
    )
    val brightnessRange = Range(
      Option(properties.getProperty(PropertyBrightnessMinValue)).map(_.toInt).getOrElse(0),
      Option(properties.getProperty(PropertyBrightnessMaxValue)).map(_.toInt).getOrElse(100)
    )

    val sigmaValue = Option(properties.getProperty(PropertyGaussSigmaValue)).map(_.toInt).getOrElse(1)

    implicit val appModule = new ImageReconstructionModule

    val listFilters = List(
      new GaussianFilterTransformer(radius, sigmaValue),
      new AvgBlurFilterTransformer(radius),
      new HueFilterImageTransformer(hueRange, saturationRange, brightnessRange)
    )

    new ImageFilterRunner(bboxesPath, originalImagesPath, destinationPath, listFilters).run
  }


}
