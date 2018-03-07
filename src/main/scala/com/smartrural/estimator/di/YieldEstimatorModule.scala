package com.smartrural.estimator.di

import com.smartrural.estimator.service._
import com.smartrural.estimator.service.impl._
import scaldi.Module

/**
  * Class that injects the appropriate service into the receiver classes
  */
class YieldEstimatorModule extends Module{
    bind[BoundingBoxService] to new BoundingBoxServiceImpl
    bind[ImageReconstructionService] to new ImageReconstructionServiceImpl()
    bind[FileManagerService] to new FileManagerServiceImpl
    bind[InferenceService] to new InferenceServiceImpl
}
