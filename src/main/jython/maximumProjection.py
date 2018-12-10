from ij import IJ;
from net.haesleinhuepf.imagej import ClearCLIJ;
from net.haesleinhuepf.imagej.kernels import Kernels;

# Init GPU
clij = ClearCLIJ.getInstance();

# get some example data
imp = IJ.openImage("http://imagej.nih.gov/ij/images/t1-head.zip");

# create and fill memory in GPU
imageInput = clij.converter(imp).getClearCLImage();
imageOutput = clij.createCLImage([imageInput.getWidth(), imageInput.getHeight()], imageInput.getChannelDataType());

# process the image
Kernels.maxProjection(clij, imageInput, imageOutput);

# show the result
clij.show(imageOutput, "output");

# get the result back as variable
result = clij.converter(imageOutput).getImagePlus();
