# CLIJ

CLIJ is an ImageJ/Fiji plugin allowing you to run OpenCL GPU accelerated code from within Fijis script editor (e.g. macro and jython). CLIJ is based on [ClearCL](http://github.com/ClearControl/ClearCL), [Imglib2](https://github.com/imglib) and [SciJava](https://github.com/SciJava).

## Accessing from ImageJ macro

Read the detailed documentation for [GPU-accelerated image processing using CLIJ from ImageJ macro](https://clij.github.io/clij-docs/).

### Installation to ImageJ/Fiji

Add the update site http://sites.imagej.net/clij to your Fiji installation. [Read more about how to activate update sites]( https://imagej.net/Following_an_update_site)

### Depending on CLIJ

If you want to access CLIJ from your Java code, it is recommended to depend on CLIJ via Maven dependencies. Add this dependency to the pom.xml file of your project:

```xml
<dependency>
  <groupId>net.haesleinhuepf</groupId>
  <artifactId>clij_</artifactId>
  <version>0.12.2</version>
</dependency>
```

To allow maven finding this artifact, add a repository to your pom.xml file:

```xml
<repository>
  <id>clij</id>
  <url>http://dl.bintray.com/haesleinhuepf/clij</url>
</repository>
```

## High level API (Java, Jython, Groovy)

When accessing [the Kernels class](https://github.com/clij/clij/blob/master/src/main/java/net/haesleinhuepf/clij/kernels/Kernels.java) from Java, Python or Groovy, also `ClearCLImage`s can be handled. To start image processing with CLIJ, first create an instance. `CLIJ.getInstance()` takes one optional parameter, which should be part of the name of the OpenCL device. The following [example](https://github.com/clij/clij/blob/master/src/main/jython/maximumProjection.py) shows how to generate a maximum projection of a stack via OpenCL.

```python
from net.haesleinhuepf.clij import CLIJ;

clij = CLIJ.getInstance();
```

Afterwards, you can convert `ImagePlus` objects to ClearCL objects wich makes them accessible on the OpenCL device:

```python
imageInput = clij.convert(imp, ClearCLBuffer);
```

Furthermore, you can create images, for example with the same size as a given one:
```python
imageOutput = clij.createCLBuffer(imageOutput);
```

Alternatively, create an image with a given size and a given type:

```python
imageOutput = clij.createCLBuffer([imageInput.getWidth(), imageInput.getHeight()], imageInput.getChannelDataType());
```

Inplace operations are not well supported by OpenCL 1.2. Thus, after creating two images, you can call a kernel taking the first image and filling the pixels of second image wiht data:

```python
from net.haesleinhuepf.clij.kernels import Kernels;

Kernels.maximumZProjection(clij, imageInput, imageOutput);
```

Then, use the `show()` method of `CLIJ` to get the image out of the GPU back to view in ImageJ:

```python
clij.show(imageOutput, "output");
```

You can also get the result image as ImagePlus:

```python
result = clij.convert(imageOutput, ImagePlus);
```

## Low level API

In order to call your own `kernel.cl` files, use the `clij.execute()` method. Example code (Jython):

```python
# initialize the GPU 
clij = CLIJ.getInstance();

# convert ImageJ image to CL images (ready for the GPU)
inputCLBuffer = clij.convert(imp, ClearCLBuffer);
outputCLBuffer = clij.create(lInputCLBuffer); # allocate memory for result image

# downsample the image stack using ClearCL / OpenCL
resultStack = clij.execute(DownsampleXYbyHalfTask, "kernels/downsampling.cl", "downsample_xy_by_half_nearest", {"src":inputCLBuffer, "dst":outputCLBuffer});

# convert the result back to imglib2 and show it
resultRAI = clij.convert(resultStack, RandomAccessibleInterval);
ImageJFunctions.show(resultRAI);
```
Complete jython examples can be found in the [src/main/jython](https://github.com/clij/clij/blob/master/src/main/jython/) directory. More Java example code can be found in the package net.haesleinhuepf.clij.demo

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
As jython is a type-agnostic programming language, CLIJ targets bringing the same convenience to OpenCL as well. However, in order to make the executed OpenCL programs image pixel type agnostic, some conventions must be introduced. The conventions are all optional. OpenCL programmers who know how to pass images of a defined type to OpenCL programs using the correct access functions can skip this section.

* Instead of using functions like `read_imagef()`, `write_imagef()`, `write_imageui()` etc.,
it is recommended to use `WRITE_IMAGE_2D()`, `WRITE_IMAGE_3D()`, `READ_IMAGE_2D()` and `READ_IMAGE_3D()` function calls. These function
calls will be replaced during runtime with the function accessing the correct image data
type. However, in order to allow CLIJ to detect the right image data type, there must
be at least two image type parameters containing "src", "dst", "input", or "output" in their
parameter names. CLIJ will then for example detect the type of an image parameter called
"src_image" and replace all calls to `READ_IMAGE_2D()` with the respective call to
`image_readui()` or `image_readf()` calls.
* Variables inside OpenCL programs can be typed with `DTYPE_IN` and `DTYPE_OUT`
instead of `float` or `int4` in order to make the OpenCL code type agnostic.


## Supported / tested platforms
There is a rudimentary list of tests implemented mainly testing conversion of types between CPU, GPU and JVM. Furthermore, there is one test applying an OpenCL kernel to images of type UnsignedShort. Following OpenCL devices were tested successfully:

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
git clone https://github.com/clij/clij
```

Open pom.xml and enter the path of your Fiji installation in the line containing

```
<imagej.app.directory>C:/path/to/Fiji.app
```

Go to the source dir and deploy to your Fiji.app

```
cd clij
deploy.bat
```

# Troubleshooting
* Fiji crashes when calling the first CLIJ filter: Check if the initialisation contains a proper name for a GPU.
* "java.io.IOException: Cannot find source: [Object] <path/filename.cl>" exception: Navigate to the jars subdirectory of your Fiji installation and locate `clearcl.jar` files, e.g. by typing `dir clearcl*` or `ls clearcl*`. If there are several versions installed, remove the older one. In order to fix this exception, you need at least `clearcl-0.5.5-RH.jar`.
* "clearcl.exceptions.ClearCLException: problem while setting argument 'parameter_of_type_float'": To hand over parameters of type float, you need to explicitly type it. Use `from java.lang import Float` and `Float(1.5)` to handover a value of 1.5 to an OpenCL parameter of type float.
* After installation, Fiji doesn't start anymore: Navigate to your Fiji folder. Check if there is clij_0.4.0.jar located in _both_ folders `plugins` and `jars`. Delete both and run the installation instructions again.
* ClearVolume doesn't work anymore. CLIJ needs developmental versions of dependencies, also ClearVolume needs. If both update sites are activated, Fiji may crash. Install only one of both at a time.
* CLIJ throws various exceptions, like CL_OUT_OF_HOST_MEMORY on Linux. Try installing an OpenCL-driver such as beignet. On Fedora 27 Linux, this command list helped (warning, don't execute these commands if you don't know what they are doing. [Read the BSD3 license file](license.txt) for details on what why we're not responsible for your actions on your computer ):

```
sudo yum install ocl-icd-devel
sudo yum install cmake
sudo yum install llvm
sudo yum install llvm-devel
sudo yum install libdrm libdrm-devel
sudo yum install libXext-devel
sudo yum install libXfixes-devel
sudo yum install clang-devel

git clone https://github.com/intel/beignet.git
cd beignet/
mkdir build
cd build
cmake ../
make
sudo make install

```
More info can be found on the website of the [beignet project](https://www.freedesktop.org/wiki/Software/Beignet/).

