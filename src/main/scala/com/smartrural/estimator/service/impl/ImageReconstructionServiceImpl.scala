package com.smartrural.estimator.service.impl

import java.io.File

import com.smartrural.estimator.model.{BBoxItemInfo, BinaryPixel}
import com.smartrural.estimator.service.{FileManagerService, ImageReconstructionService}
import com.smartrural.estimator.util.AppConstants.{RedColor, VoidColor}
import com.smartrural.estimator.util.ImageUtils
import com.smartrural.estimator.util.ImageUtils._
import org.apache.commons.io.filefilter.IOFileFilter
import org.opencv.core.Mat
import scaldi.{Injectable, Injector}

class ImageReconstructionServiceImpl(implicit inj:Injector) extends ImageReconstructionService with Injectable{

  val fileManager = inject[FileManagerService]

  override def reconstructImage(radius:Int,
                                originalImageFile:File,
                                patchesInfoList:List[BBoxItemInfo],
                                patchesPath:File,
                                destinationPath:File):Unit = {
    val originalImage = fileManager.readImage(originalImageFile)
    val patchesImages = retrievePatchesForImage(patchesPath.getAbsolutePath, originalImageFile.getName)

    val rows = originalImage.rows()
    val cols = originalImage.cols()

    val destinationFile = new File(destinationPath, originalImageFile.getName.replace(".jpg", ".png"))

    if(destinationFile.exists()) {
      destinationPath.mkdirs()
      val nonVoidPx = getColoredPixelsFromList(patchesImages, patchesInfoList, rows, cols)
      val pixelToInspect = nonVoidPx.filter(px => px.considerInCalculation(nonVoidPx))

      val dstImg = getMat(rows, cols)
      ImageUtils
        .extractAllSurroundingVoidPixelsFromImage(pixelToInspect, nonVoidPx, rows, cols, radius)
        .foreach(px => dstImg.put(px.row, px.col, RedColor))

      fileManager.writeImage(dstImg, destinationFile)
    }
  }

  /**
    * get the patch images corresponding to the image name passed
    * @param patchesPath the root path of the patches
    * @param imageName the image name
    * @return the list of patch images
    */
  def retrievePatchesForImage(patchesPath:String, imageName:String):List[Mat] ={
    val fileFilter = new IOFileFilter {
      override def accept(file: File) =
        file.getName.startsWith(imageName.substring(0, imageName.lastIndexOf(".")).concat("_"))
      override def accept(dir: File, name: String) = accept(new File(dir, name))
    }

    fileManager.getChildList(patchesPath, fileFilter).map(patchFile => fileManager.readImage(patchFile)).toList
  }

  /**
    * Creates the complete image containing the patches in its apropriate position
    * @param patchImagesList the patches to put on the final image
    * @param inferenceInfoList the inference containing information about the patches
    */
  def getColoredPixelsFromList(patchImagesList: List[Mat],
                                inferenceInfoList:List[BBoxItemInfo], rows:Int, cols:Int):List[BinaryPixel] =
    inferenceInfoList.flatMap(inferenceInfo => {
      findMatchingMatByResolution(patchImagesList, inferenceInfo)
        .map(mat => getNonVoidPixelsWithOffset(mat, inferenceInfo))
    }).flatten

  /**
    * find the corresponding image in the list of patches which eight and width is the same than the one contained in
    * the bboxItemInfo object passed
    * @param patchImagesList the image list
    * @param info the bboxItemInfo
    * @return an option containing the image
    */
  private def findMatchingMatByResolution(patchImagesList:List[Mat],
                                          info:BBoxItemInfo):Option[Mat] =
    patchImagesList.find(mat => mat.rows == info.ImageHeight && mat.cols == info.ImageWidth)

  /**
    * writes the given patch image into the image destination according to the bboxItemInfo information passed
    * @param img the patch image
    * @param info the bboxItemInfo object
    */
  private def getNonVoidPixelsWithOffset(img:Mat, info:BBoxItemInfo):List[BinaryPixel] =
    getMatAsBinaryPixels(img)
      .filter(_.isNotVoid)
      .map(pixel => BinaryPixel(false, info.getRowWithOffset(pixel.row), info.getColWithOffset(pixel.col)))

  /**
    * Sets the color of the pixel to red if it is not void
    * @param pixel the pixel to inspect
    * @return an array representing either void or red color
    */
  private def getColorForPixel(pixel:BinaryPixel):Array[Byte] = if (pixel.isVoid) VoidColor else RedColor
}
