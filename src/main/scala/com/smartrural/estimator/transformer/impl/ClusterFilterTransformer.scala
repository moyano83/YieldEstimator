package com.smartrural.estimator.transformer.impl

import java.io.File

import com.smartrural.estimator.service.FileManagerService
import com.smartrural.estimator.transformer.ImageTransformer
import com.smartrural.estimator.util.{AppConstants, ImageUtils}
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

/**
  * Created by jm186111 on 10/03/2018.
  */
class ClusterFilterTransformer(reconstructedImagesPath:String)(implicit val inj:Injector)
  extends ImageTransformer with Injectable{

  val fileManagerService = inject[FileManagerService]
  /**
    * Transform an imagen according to the internal implementation of the Transformer
    *
    * @param matFile File representing the image
    * @param img     the image to transform
    * @return the transformed image
    */
  override def transform(matFile: File, img: Mat): Mat = {
    val maskImageFile = fileManagerService.getMirrorImageFileWithExtension(matFile, reconstructedImagesPath, AppConstants.FormatPng)
    val maskMat = fileManagerService.readImage(maskImageFile)

    val dstMat = ImageUtils.getMat(img.rows, img.cols)
    img.copyTo(dstMat, maskMat)

    dstMat
  }
}
