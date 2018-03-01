# YieldEstimator

## Motivation
The following project aims to provide with a set of applications to estimate the yield throughput of a vineyard plot, given the following files:

- Set of original pictures from which the cluster estimation process was derived
- Set of files containing detailed information about the cluster detections as well as the position whithin the original picture
- Set of binary pictures (red and black pixels) containing the detected grape clusters
- GeoJSON File containing the result of the inference process specifying the number of pixels corresponding to grape clusters per picture, as well as the information about where the pixel was taken (latitude, longitude).

## Application Execution

### Image Reconstruction application
Application that construct a binary image containing only the cluster inferences, the application is run using the 
command: 

`scala com.smartrural.estimator.ImageReconstructionApp <Conf_file_path>`

the `<Conf_file_path>` is the path to the configuration file that contains the following parameters:

    * bbox.file.path: path to the root of the bounding boxes files
    * original.image.path: path to the root folder containing the original images
    * patch.image.path: path to the root folder containing the inference patches
    * destination.file.path: path to the root folder where the reconstructed images would be placed

### Image Transform application
Applies a series of filters to the given images in order to detect the leafs contained on them. The application can 
be run with the following command:

`scala com.smartrural.estimator.ImageTransformApp <Conf_file_path>`

the `<Conf_file_path>` is the path to the configuration file that contains the following parameters:

    * original.image.path: path to the root folder containing the original images
    * bbox.file.path: path to the root of the bounding boxes files
    * destination.file.path: path to the root folder where the filtered images would be placed
    * radius.pixel.locator: radius (in pixels) to consider while doing the image transformations
    * hue.min.value: Used by the HueFilterImageTransformer, specifies the min value of hue to consider on range 
    filtering 
    * hue.max.value: Used by the HueFilterImageTransformer, specifies the max value of hue to consider on range 
    filtering
    * saturation.min.value: Used by the HueFilterImageTransformer, specifies the min value of saturation to consider
     on range filtering
    * saturation.max.value: Used by the HueFilterImageTransformer, specifies the max value of saturation to consider
    on range filtering
    * brightness.min.value: Used by the HueFilterImageTransformer, specifies the min value of brightness to consider
    on range filtering
    * brightness.max.value: Used by the HueFilterImageTransformer, specifies the max value of brightness to consider
    on range filtering
    * gauss.sigma.value: Value of the gamma factor (standard deviation) to use on the Gauss filter transformation
    
### Pixel Image locator app
Extracts the percentage of leaf pixels that surrounds the detected clusters in a picture. The application can be run with the following command:
                                                                                        
`scala com.smartrural.estimator.ImageTransformApp <Conf_file_path>`
                                                                                        
the `<Conf_file_path>` is the path to the configuration file that contains the following parameters: 
    
    * radius.pixel.locator: radius (in pixels) to consider while doing the image transformations
    * inferences.file.path: path to the root folder where the reconstructed images are
    * filtered.image.path: path to the root folder where the filtered images are
    * reconstructed.image.path:
    * destination.file.path: path to the file to be written by the process