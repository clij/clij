# this example ImageJ/Fiji jython script shows how to downsample an
# open image by half using an OpenCL kernel.

from fastfuse.tasks import DownsampleXYbyHalfTask;
from clearcl.imagej import ClearCLIJ;
from net.imglib2 import RandomAccessibleInterval;
from net.imglib2.img.display.imagej import ImageJFunctions;
from net.imglib2.type.numeric.integer import UnsignedShortType;
from net.imglib2.view import Views;
from ij import IJ;

# take the current image which is open in ImageJ
lImagePlus = IJ.openImage("http://imagej.nih.gov/ij/images/t1-head.zip");

# initialize ClearCL context and convenience layer
clij = ClearCLIJ.getInstance();

# convert imglib2 image to CL images (ready for the GPU)
inputCLImage = clij.converter(lImagePlus).getClearCLImage();
outputCLImage = clij.createCLImage([inputCLImage.getWidth() / 2, inputCLImage.getHeight() / 2, inputCLImage.getDepth() / 2], inputCLImage.getChannelDataType());

# downsample the image stack using ClearCL / OpenCL
clij.execute(DownsampleXYbyHalfTask, "kernels/downsampling.cl", "downsample_xy_by_half_nearest", {"src":inputCLImage, "dst":outputCLImage});

# convert the result back to imglib2 and show it
resultRAI = clij.converter(outputCLImage).getRandomAccessibleInterval();
ImageJFunctions.show(resultRAI);