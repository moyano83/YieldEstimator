package com.smartrural.estimator.di

import com.smartrural.estimator.service.{BoundingBoxService, ImageReconstructionService}
import com.smartrural.estimator.service.impl.{BoundingBoxTextReaderService, LocalImageReconstructionService}
import scaldi.Module

/**
  * Created by jm186111 on 31/01/2018.
  */
class AppModule extends Module{
    bind[BoundingBoxService] to new BoundingBoxTextReaderService
    bind[ImageReconstructionService] to new LocalImageReconstructionService()
}
