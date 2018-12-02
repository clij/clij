# ClearCLIJ

ClearCLIJ is an ImageJ/Fiji plugin allowing you to run OpenCL GPU accelerated code from within Fijis script editor (e.g. macro and jython). ClearCLIJ is based on [ClearCL](http://github.com/ClearControl/ClearCL), [FastFuse](https://github.com/ClearControl/FastFuse), [Imglib2](https://github.com/imglib) and [SciJava](https://github.com/SciJava).

## Accessing from ImageJ macro

The ImageJ macro extensions allow access to all methods in [the CLIJMacroAPI class](https://github.com/haesleinhuepf/clearclij/blob/master/src/main/java/net/haesleinhuepf/imagej/macro/CLIJMacroAPI.java). See a detailed list below. This allows basic operations such as mathematical operations on images.

Example code (ImageJ macro)
```javascript
run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);
Ext.CLIJ_push(output);

// Blur in GPU
Ext.CLIJ_blur(input, output, 20, 20, 1, 10, 10, 1);

// Get results back from GPU
Ext.CLIJ_pull(output);
```
[There is a fully functional ImageJ macro file available in this repository.](https://github.com/haesleinhuepf/clearclij/blob/master/src/main/macro/testExtensions.ijm)

### Installation to ImageJ/Fiji

Add the update site http://sites.imagej.net/clij to your Fiji installation. [Read more about how to activate update sites]( https://imagej.net/Following_an_update_site)

### Supported methods

There are five methods for memory transfer between RAM and GPU:

* `Ext.CLIJ_push(image)` sends an image with the given name to the GPU.
* `Ext.CLIJ_pull(image)` retrieves a given image from the GPU and shows it.
* `Ext.CLIJ_releas(image)` frees the memory in the GPU which is reserved for a given image.
* `Ext.CLIJ_clear()` releases the memory for all stored images in the GPU.

Furthermore, there is a `help("")` method to assist you in finding the right OpenCL kernel call for your workflow. Just enter the name of the desired operation:

```java
run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");
Ext.CLIJ_help("mean");
```

```java
Found 3 method(s) containing the pattern "mean":
Ext.CLIJ_mean2d(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_mean3d(Image source, Image destination, Number radiusX, Number radiusY, Number radius Z);
Ext.CLIJ_meanSliceBySlice(Image source, Image destination, Number radiusX, Number radiusY);
```

The full list of supported kernels can be retrieved by calling `help("");`

```java
Found 54 method(s) containing the pattern "":
Ext.CLIJ_absolute(Image source, Image destination);
Ext.CLIJ_addPixelwise(Image summand1, Image summand2, Image destination);
Ext.CLIJ_addScalar(Image source, Image destination, Number scalar);
Ext.CLIJ_addWeightedPixelwise(Image summand1, Image summand2, Image destination, Number factor1, Number factor2);
Ext.CLIJ_argMaxProjection(Image source, Image destination_max, Image destination_arg_max);
Ext.CLIJ_binaryAnd(Image source1, Image source2, Image destination);
Ext.CLIJ_binaryNot(Image source, Image destination);
Ext.CLIJ_binaryOr(Image source1, Image source2, Image destination);
Ext.CLIJ_blur2d(Image source, Image destination, Number radiusX, Number radiusY, Number sigmaX, Number sigmaY);
Ext.CLIJ_blur3d(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ, Number sigmaX, Number sigmaY, Number sigmaZ);
Ext.CLIJ_copy(Image source, Image destination);
Ext.CLIJ_copySlice(Image source, Image destination, Number sliceIndex);
Ext.CLIJ_crop2d(Image source, Image destination, Number startX, Number startY);
Ext.CLIJ_crop3d(Image source, Image destination, Number startX, Number startY, Number startZ);
Ext.CLIJ_detectMaxima(Image source, Image destination, Number radius);
Ext.CLIJ_detectMaximaSliceBySlice(Image source, Image destination, Number radius);
Ext.CLIJ_detectMinima(Image source, Image destination, Number radius);
Ext.CLIJ_detectMinimaSliceBySlice(Image source, Image destination, Number radius);
Ext.CLIJ_dilate(Image source, Image destination);
Ext.CLIJ_dividePixelwise(Image dividend, Image divisor, Image destination);
Ext.CLIJ_downsample2d(Image source, Image destination, Number factorX, Number factorY);
Ext.CLIJ_downsample3d(Image source, Image destination, Number factorX, Number factorY, Number factorZ);
Ext.CLIJ_downsampleSliceBySliceHalfMedian(Image source, Image destination);
Ext.CLIJ_erode(Image source, Image destination);
Ext.CLIJ_flip2d(Image source, Image destination, Boolean flipX, Boolean flipY);
Ext.CLIJ_flip3d(Image source, Image destination, Boolean flipX, Boolean flipY, Boolean flipZ);
Ext.CLIJ_invertBinary(Image source, Image destination);
Ext.CLIJ_mask(Image source, Image mask, Image destination);
Ext.CLIJ_maskStackWithPlane(Image source3d, Image mask2d, Image destination3d);
Ext.CLIJ_maxPixelwise(Image source, Image source2, Image destination);
Ext.CLIJ_maxProjection(Image source, Image destination);
Ext.CLIJ_maxProjectionDimSelect(Image source, Image destination, Number projectedX, Number projectedY, Number projectedDimension);
Ext.CLIJ_maximum2d(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_maximum3d(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ);
Ext.CLIJ_maximumSliceBySlice(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_mean2d(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_mean3d(Image source, Image destination, Number radiusX, Number radiusY, Number radius Z);
Ext.CLIJ_meanSliceBySlice(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_median2d(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_median3d(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ);
Ext.CLIJ_medianSliceBySlice(Image source, Image destination, Number radiusX, Number radiusY)
Ext.CLIJ_minimum2d(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_minimum3d(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ);
Ext.CLIJ_minimumSliceBySlice(Image source, Image destination, Number radiusX, Number radiusY);
Ext.CLIJ_multiplyPixelwise(Image factor1, Image factor2, Image destination);
Ext.CLIJ_multiplyScalar(Image source, Image destination);
Ext.CLIJ_multiplyStackWithPlane(Image source3d, Image source2d, Image destination3d);
Ext.CLIJ_power(Image source, Image destination);
Ext.CLIJ_resliceBottom(Image source, Image destination);
Ext.CLIJ_resliceLeft(Image source, Image destination);
Ext.CLIJ_resliceRight(Image source, Image destination);
Ext.CLIJ_resliceTop(Image source, Image destination);
Ext.CLIJ_set(Image image, Number value);
Ext.CLIJ_threshold(Image source, Image destination, Number Threshold);
```


## High level API (Java, Jython, Groovy)
When accessing [the Kernels class](https://github.com/haesleinhuepf/clearclij/blob/master/src/main/java/net/haesleinhuepf/imagej/kernels/Kernels.java) from Java, Python or Groovy, also `ClearCLImage`s can be handled. To start image processing with ClearCLIJ, first create an instance. `ClearCLIJ.getInstance()` takes one optional parameter, which should be part of the name of the OpenCL device. The following [example](https://github.com/haesleinhuepf/clearclij/blob/master/src/main/jython/maximumProjection.py) shows how to generate a maximum projection of a stack via OpenCL.

```python
from clearcl.imagej import ClearCLIJ;

clij = ClearCLIJ.getInstance();
```

Afterwards, you can convert `ImagePlus` objects to ClearCL objects wich makes them accessible on the OpenCL device:

```python
imageInput = clij.converter(imp).getClearCLImage();
```

Furthermore, you can create images, for example with the same size as a given one:
```python
imageOutput = clij.createClearCLImage(imageOutput);
```

Alternatively, create an image with a given size and a given type:

```python
imageOutput = clij.createCLImage([imageInput.getWidth(), imageInput.getHeight()], imageInput.getChannelDataType());
```

Inplace operations are not well supported by OpenCL 1.2. Thus, after creating two images, you can call a kernel taking the first image and filling the pixels of second image wiht data:

```python
from clearcl.imagej.kernels import Kernels;
Kernels.maxProjection(clij, imageInput, imageOutput);
```

Then, use the `show()` method of `ClearCLIJ` to get the image out of the GPU back to view in ImageJ:

```python
clij.show(imageOutput, "output");
```

You can also get the result image as ImagePlus:

```python
result = clij.converter(imageOutput).getImagePlus();
```

## Low level API

In order to call your own `kernel.cl` files, use the `clij.execute()` method. Example code (Jython):

```python
# initialize the GPU 
clij = ClearCLIJ.getInstance();

# convert ImageJ image to CL images (ready for the GPU)
lInputCLImage = clij.converter(imp).getClearCLImage();
lOutputCLImage = clij.converter(imp).getClearCLImage(); # copy again to allocate memory for result image

# downsample the image stack using ClearCL / OpenCL
resultStack = clij.execute(DownsampleXYbyHalfTask, "kernels/downsampling.cl", "downsample_xy_by_half_nearest", {"src":lInputCLImage, "dst":lOutputCLImage});

# convert the result back to imglib2 and show it
resultRAI = clij.converter(resultStack).getRandomAccessibleInterval();
ImageJFunctions.show(resultRAI);
```
Complete jython examples can be found in the [src/main/jython](https://github.com/haesleinhuepf/clearclij/blob/master/src/main/jython/) directory. More Java example code can be found in the package net.haesleinhuepf.imagej.demo

## OpenCL Kernel calls with CLIJ.execute()
The execute function asks for three or four parameters
```
clij.execute(<Class>, "filename_open.cl", "kernelfunction", {"src":image, "dst":image, "more":5, "evenmore":image})

clij.execute("absolute/or/relative/path/filename_open.cl", "kernelfunction", {"src":image, "dst":image, "more":5, "evenmore":image})
```
* An optional class file as an anchor to have a point for where to start
  searching for the program file (second parameter).
* The open.cl program file will be searched for in the same folder where the
  class (first parameter) is defined. In the first example above, this class
  comes with the dependency FastFuse. Alternatively, an absolute path can be
  proveded if there is no class given as first parameter. In case a relative
  path is given, it must be relative from the current dir of Fiji.
* The name of the kernel function defined in the program file
* A dictionary with all the parameters of the kernel function, such as
  "src" and "dst". It is recommended to have at least a "src" and a "dst"
  parameter, because CLIJ derives image data types and global space from
  these parameters.

## Type agnostic OpenCL
As jython is a type-agnostic programming language, ClearCLIJ targets bringing the same convenience to OpenCL as well. However, in order to make the executed OpenCL programs image pixel type agnostic, some conventions must be introduced. The conventions are all optional. OpenCL programmers who know how to pass images of a defined type to OpenCL programs using the correct access functions can skip this section.

* Instead of using functions like `read_imagef()`, `write_imagef()`, `write_imageui()` etc.,
it is recommended to use `WRITE_IMAGE()` and `READ_IMAGE()` function calls. These function
calls will be replaced during runtime with the function accessing the correct image data
type. However, in order to allow ClearCLIJ to detect the right image data type, there must
be at least two image type parameters containing "src", "dst", "input", or "output" in their
parameter names. ClearCLIJ will then for example detect the type of an image parameter called
"src_image" and replace all calls to `READ_IMAGE()` with the respective call to
`image_readui()` or `image_readf()` calls.
* Variables inside OpenCL programs can be typed with `DTYPE_IN` and `DTYPE_OUT`
instead of `float` or `int4` in order to make the OpenCL code type agnostic.


## Supported / tested platforms
There is a rudimentary list of tests implemented in the clearcl.imagej.test package mainly testing conversion of types between CPU, GPU and JVM. Furthermore, there is one test applying an OpenCL kernel to images of type UnsignedShort. Following OpenCL devices were tested successfully:

* AMD Radeon RX Vega 3 (OpenCL 2.0, Win 10 64 bit, Nov 2018)
* Nvidia GeForce 940MX (OpenCL 2.0, Win 10 64 bit, Apr 2018)
* NVidia GeForce GTX 960M (OpenCL 1.2, Win 10 64 bit, Feb 2018)
* Intel(R) HD Graphics 620 (OpenCL 2.0, Fedora 27, Nov 2018)
* Intel(R) HD Graphics 620 (OpenCL 1.2, Win 10 64bit, Sept 2018)
* Intel(R) HD Graphics 530 (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) HD Graphics 515 (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) HD Graphics 405 (OpenCL 1.2, Win 10 64 bit, Feb 2018)
* Intel(R) HD Graphics 400 (OpenCL 1.2, Win 10 64 bit, Nov 2018)
* Intel(R) Core(TM) i7-7500U CPU @ 2.70GHz (OpenCL 1.2, Win 10 64 bit, Apr 2018)
* Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) Core(TM) m3-6Y30 CPU @ 0.90GHz (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) Atom(TM) x7-Z8750  CPU @ 1.60GHz (OpenCL 1.2, Win 10 64 bit, Feb 2018)
* Intel(R) Atom(TM) x5-Z8350  CPU @ 1.44GHz (OpenCL 1.2, Win 10 64 bit, Nov 2018)

Tests failed on these devices:

* AMD Ryzen 3 (OpenCL 1.2, Win 10 64 bit, Sept 2018)
* AMD A10-8700P Radeon R6, 10 Compute Cores 4C+6G (OpenCL 1.2, Win 10 64 bit, Feb 2018)
* Intel(R) Core(TM) i7-4980HQ CPU @ 2.80GHz (OpenCL 1.2, macOS 10.12.6, Feb 2018)
* Intel(R) Core(TM) i7-8650U CPU @ 1.90GHz (OpenCL 1.2, Win 10 64 bit, Mar 2018)
* Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz (OpenCL 2.0, Fedora 27, Apr 2018)

## Installation using maven

Clone this repo
```
git clone https://github.com/haesleinhuepf/ClearCLIJ
```

Open pom.xml and enter the path of your Fiji installation in the line containing

```
<imagej.app.directory>C:/path/to/Fiji.app
```

Go to the source dir and deploy to your Fiji.app

```
cd ClearCLIJ
deploy.bat
```

# Troubleshooting
* Fiji crashes when calling the first ClearCLIJ filter: Check if the initialisation contains a proper name for a GPU.
* "java.io.IOException: Cannot find source: [Object] <path/filename.cl>" exception: Navigate to the jars subdirectory of your Fiji installation and locate `clearcl.jar` files, e.g. by typing `dir clearcl*` or `ls clearcl*`. If there are several versions installed, remove the older one. In order to fix this exception, you need at least `clearcl-0.5.5-RH.jar`.
* "clearcl.exceptions.ClearCLException: problem while setting argument 'parameter_of_type_float'": To hand over parameters of type float, you need to explicitly type it. Use `from java.lang import Float` and `Float(1.5)` to handover a value of 1.5 to an OpenCL parameter of type float.
* After installation, Fiji doesn't start anymore: Navigate to your Fiji folder. Check if there is clij_0.4.0.jar located in _both_ folders `plugins` and `jars`. Delete both and run the installation instructions again.
* ClearVolume doesn't work anymore. ClearCLIJ needs developmental versions of dependencies, also ClearVolume needs. If both update sites are activated, Fiji may crash. Install only one of both at a time.


