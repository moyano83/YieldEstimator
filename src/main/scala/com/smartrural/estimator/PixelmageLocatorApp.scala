package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import com.smartrural.estimator.di.ImageReconstructionModule
import com.smartrural.estimator.runner.PixelLocatorRunner
import com.smartrural.estimator.service.ColorDetectionService
import com.smartrural.estimator.service.impl.BoundedColorDetectionService
import com.smartrural.estimator.util.AppConstants
import org.slf4j.LoggerFactory
import scaldi.Module

object PixelmageLocatorApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.PixelmageLocatorApp <Conf_path>'")
      System.exit(1)
    }



    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))

    val originalImagesPath = properties.getProperty(AppConstants.PropertyOriginalImagePath)
    val binaryImagesPath = properties.getProperty(AppConstants.BinaryImagesPath)
    val destinationPath = properties.getProperty(AppConstants.DestinationPath)
    val radius = properties.getProperty(AppConstants.RadiusPixelLocator)
    //Getting the color ranges, if not set, they will be unbounded
    val hueRange = Range(
      Option(properties.getProperty(AppConstants.HueMinValue)).map(_.toInt).getOrElse(0),
      Option(properties.getProperty(AppConstants.HueMaxValue)).map(_.toInt).getOrElse(360)
    )
    val saturationRange = Range(
      Option(properties.getProperty(AppConstants.SaturationMinValue)).map(_.toInt).getOrElse(0),
      Option(properties.getProperty(AppConstants.SaturationMaxValue)).map(_.toInt).getOrElse(100)
    )
    val brightnessRange = Range(
      Option(properties.getProperty(AppConstants.BrightnessMinValue)).map(_.toInt).getOrElse(0),
      Option(properties.getProperty(AppConstants.BrightnessMaxValue)).map(_.toInt).getOrElse(100)
    )

    implicit val appModule = new ImageReconstructionModule :: new Module{
      bind[ColorDetectionService] to new BoundedColorDetectionService(hueRange, saturationRange, brightnessRange)
    }

    new PixelLocatorRunner(originalImagesPath, binaryImagesPath, destinationPath, radius.toInt).run
  }


}
