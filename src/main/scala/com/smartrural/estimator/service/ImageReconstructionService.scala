package com.smartrural.estimator.service

import java.io.File

import com.smartrural.estimator.model.BBoxItemInfo

/**
  * Service that provides functionality to construct the binary images from the patch images
  */
  trait ImageReconstructionService {
  /**
    * Creates a full binary image from the patches and the information contained in the bboxFile provided
    * @param radius the radius of the cluster to consider
    * @param originalImgFile the path to the original image
    * @param inferences List of inferences for the image
    * @param patchesPath the path to the patch images
    * @param destinationPath the destination path to write the final image
    */
  def reconstructImage(radius:Int,
                       originalImgFile:File,
                       inferences:List[BBoxItemInfo],
                       patchesPath:File,
                       destinationPath:File):Unit
}
