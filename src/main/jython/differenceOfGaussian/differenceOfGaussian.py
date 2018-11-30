# this example ImageJ/Fiji jython script shows how to apply a
# difference of gaussian (DoG) filter to an image using an
# OpenCL kernel.
#
# Author: Robert Haase (@haesleinhuepf)
# March 2018
#

from net.haesleinhuepf.imagej import ClearCLIJ;
from net.imglib2 import RandomAccessibleInterval;
from net.imglib2.img.display.imagej import ImageJFunctions;
from net.imglib2.type.numeric.integer import UnsignedShortType;
from net.imglib2.view import Views;
from ij import IJ;
from java.lang import Float
import os;
import inspect

# retrieve the folder where this script is located (thanks to @mountain_man from the ImageJ forum)
filesPath = os.path.dirname(os.path.abspath(inspect.getsourcefile(lambda:0))) + "/"

# take the current image which is open in ImageJ
imp = IJ.openImage("http://imagej.nih.gov/ij/images/t1-head.zip");

# initialize ClearCL context and convenience layer
clij = ClearCLIJ.getInstance();

# convert imglib2 image to CL images (ready for the GPU)
lInputCLImage = clij.converter(imp).getClearCLImage();
lOutputCLImage = clij.createCLImage([lInputCLImage.getWidth(), lInputCLImage.getHeight()], lInputCLImage.getChannelDataType());

# apply a filter to the image using ClearCL / OpenCL
clij.execute(filesPath + "differenceOfGaussian.cl", "subtract_convolved_images_2d_fast", {
"input":lInputCLImage,
"output":lOutputCLImage,
"radius":6,
"sigma_minuend":Float(1.5),
"sigma_subtrahend":Float(3)});

# convert the result back to imglib2 and show it
resultRAI = clij.converter(lOutputCLImage).getRandomAccessibleInterval();
ImageJFunctions.show(resultRAI);
IJ.run("Enhance Contrast", "saturated=0.35");

