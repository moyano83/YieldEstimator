package com.smartrural.estimator.service

import java.io.File

import com.smartrural.estimator.model.BBoxItemInfo

/**
  * Created by jm186111 on 31/01/2018.
  */
trait ImageReconstructionService {
  /**
    * Creates a full binary image from the patches and the information contained in the bboxFile provided
    * @param originalImageFile the path to the original image
    * @param inferences List of inferences for the image
    * @param patchesPath the path to the patch images
    * @param destinationPath the destination path to write the final image
    * @return true if the operation was executed successfully
    */
  def reconstructImage(originalImageFile:File,
                       inferences:List[BBoxItemInfo],
                       patchesPath:File,
                       destinationPath:File):Boolean
}
