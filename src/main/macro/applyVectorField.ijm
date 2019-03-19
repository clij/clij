// This script demonstrates how to apply a 
// vector field in order to transform it non-rigidly
//
// Author: Robert Haase, rhaase@mpi-cbg.de
// March 2019
//

run("Close All");

// get test image
run("Blobs (25K)");
run("32-bit");
rename("blobs");

// create two images describing local shift
newImage("shiftX", "32-bit black", 256, 254, 1);
newImage("shiftY", "32-bit black", 256, 254, 1);

// shift some of the pixels in X
selectImage("shiftX");
makeOval(76, 98, 72, 68);
run("Add...", "value=25");
run("Select None");
run("Gaussian Blur...", "sigma=5");
run("Enhance Contrast", "saturated=0.35");

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_push("blobs");
Ext.CLIJ_push("shiftX");
Ext.CLIJ_push("shiftY");

// apply transform
Ext.CLIJ_applyVectorField2D("blobs", "shiftX", "shiftY", "transformed");

// get result back from GPU
Ext.CLIJ_pull("transformed");
run("Invert LUT");
