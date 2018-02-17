
from fastfuse.tasks import DownsampleXYbyHalfTask;
from clearcl.imagej import ClearCLIJ;
from net.imglib2 import RandomAccessibleInterval;
from net.imglib2.img.display.imagej import ImageJFunctions;
from net.imglib2.type.numeric.integer import UnsignedShortType;
from net.imglib2.view import Views;
from ij import IJ;

# take the current image which is open in ImageJ
lImagePlus = IJ.getImage()

# initialize ClearCL context and convenience layer
lCLIJ = ClearCLIJ.getInstance();

# convert imglib2 image to clearcontrol stack
lInputCLImage = lCLIJ.converter(lImagePlus).getOffHeapPlanarStack();

lOutputCLImage = lCLIJ.converter(lImagePlus).getOffHeapPlanarStack();

# downsample the image stack using ClearCL / OpenCL
lResultStack = lCLIJ.execute(DownsampleXYbyHalfTask, "kernels/downsampling.cl", "downsample_xy_by_half_nearest", {"src":lInputCLImage, "dst":lOutputCLImage});

# convert the result back to imglib2 and show it
lResultImg = lCLIJ.converter(lResultStack).getRandomAccessibleInterval();
ImageJFunctions.show(lResultImg);