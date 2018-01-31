package com.smartrural.estimator.service

import java.io.File

/**
  * Created by jm186111 on 31/01/2018.
  */
trait ImageReconstructionService {
  def reconstructImage(originalImageFile:File, bboxesFilePath:File, patchesPath:File, destinationPath:File):Boolean
}
