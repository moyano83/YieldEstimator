package com.smartrural.estimator.di

import com.smartrural.estimator.service._
import com.smartrural.estimator.service.impl._
import scaldi.Module

/**
  * Created by jm186111 on 31/01/2018.
  */
class ImageReconstructionModule extends Module{
    bind[BoundingBoxService] to new BoundingBoxTextReaderService
    bind[ImageReconstructionService] to new LocalImageReconstructionService()
    bind[PixelLocatorService] to new ImagePixelLocatorService()
    bind[FileManagerService] to new LocalFileManager
    bind[ColorDetectionService] to new PassAllColorService
}