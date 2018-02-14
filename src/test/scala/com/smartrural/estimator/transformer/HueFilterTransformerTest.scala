package com.smartrural.estimator.transformer

import java.io.File
import javax.imageio.ImageIO

import com.smartrural.estimator.service.FileManagerService
import com.smartrural.estimator.service.impl.LocalFileManager
import com.smartrural.estimator.util.AppConstants.JpgFormat
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import scaldi.Module

/**
  * Created by jm186111 on 12/02/2018.
  */
@RunWith(classOf[JUnitRunner])
class HueFilterTransformerTest extends FlatSpec with MockFactory{

  val rootPathFile = new File(getClass.getClassLoader.getResource(".").getPath)

  val image = new File(rootPathFile, "original_images/valdemonjas-2017-09-13_01/z-img-000-000004.jpg")

  implicit val inj = new Module{
    bind[FileManagerService] to new LocalFileManager
  }

  val hueFilter = new HueFilterImageTransformer(Range(60,170), Range(0,100), Range(0,100))

  behavior of "HueFilterImageTransformer"

  it should "run the hue filter" in {
    val dstImage = "z-img-000-000004-hue.jpg"
    val dstFile = new File(rootPathFile, dstImage)
    val originalImage = ImageIO.read(image)
    ImageIO.write(hueFilter.transform(originalImage), JpgFormat, dstFile)
    assert(dstFile.exists())
  }
}
