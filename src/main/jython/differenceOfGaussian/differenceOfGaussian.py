# this example ImageJ/Fiji jython script shows how to apply a
# difference of gaussian (DoG) filter to an image using an
# OpenCL kernel.
#
# Author: Robert Haase (@haesleinhuepf)
# March 2018
#

from clearcl.imagej import ClearCLIJ;
from net.imglib2 import RandomAccessibleInterval;
from net.imglib2.img.display.imagej import ImageJFunctions;
from net.imglib2.type.numeric.integer import UnsignedShortType;
from net.imglib2.view import Views;
from ij import IJ;
from java.lang import Float
import os;

# retrieve the folder where this script is located (thanks to @mountain_man from the ImageJ forum)
import inspect
print inspect.getsourcefile(lambda:0)
lCLFilesPath = os.path.dirname(os.path.abspath(inspect.getsourcefile(lambda:0))) + "/"

#lCLFilesPath = "/home/rhaase/code/ClearCLIJ/src/main/jython/differenceOfGaussian/"

# take the current image which is open in ImageJ
lImagePlus = IJ.getImage()

# initialize ClearCL context and convenience layer
lCLIJ = ClearCLIJ.getInstance();

print(lCLIJ.getClearCLContext())

# convert imglib2 image to CL images (ready for the GPU)
lInputCLImage = lCLIJ.converter(lImagePlus).getClearCLImage();
lOutputCLImage = lCLIJ.converter(lImagePlus).getClearCLImage();

# apply a filter to the image using ClearCL / OpenCL
lCLIJ.execute(lCLFilesPath + "differenceOfGaussian.cl", "subtract_convolved_images_2d_fast", {
"input":lInputCLImage,
"output":lOutputCLImage,
"radius":6,
"sigma_minuend":Float(1.5),
"sigma_subtrahend":Float(3)});

# convert the result back to imglib2 and show it
lResultImg = lCLIJ.converter(lOutputCLImage).getRandomAccessibleInterval();
ImageJFunctions.show(lResultImg);