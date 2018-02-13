package com.smartrural.estimator

import java.io.{File, FileInputStream}
import java.util.Properties

import org.slf4j.LoggerFactory

object PixelImageLocatorApp {

  val logger = LoggerFactory.getLogger(getClass)

  def main(args:Array[String]):Unit = {

    if(args.length!=1) {
      logger.error("Invalid number of parameters. USAGE: 'com.smartrural.estimator.PixelmageLocatorApp <Conf_path>'")
      System.exit(1)
    }

    val properties = new Properties()
    properties.load(new FileInputStream(new File(args(0))))


  }


}
