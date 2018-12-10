// ClearCLIJ example macro: orthogonalMaximumProjections.ijm
//
// This macro shows how maximum-X, -Y and -Z projections can be created using the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------


// Get test data
run("T1 Head (2.4M, 16-bits)");
//open("C:/structure/data/t1-head.tif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();
downScalingFactorInXY = 0.666; // because the image has slice distance 1.5
downScalingFactorInZ = 1;

// create memory for downsampled image
newImage("Untitled", "16-bit black", width * downScalingFactorInXY, height * downScalingFactorInXY, slices * downScalingFactorInZ);
rename("Downscaled");
downscaled = getTitle();

// create memory for result images
newImage("Untitled", "16-bit black", slices * downScalingFactorInZ, height * downScalingFactorInXY, 1);
rename("Maximum projection in X");
maximumProjectionX = getTitle();

newImage("Untitled", "16-bit black", slices * downScalingFactorInZ, width * downScalingFactorInXY, 1);
rename("Maximum projection in Y");
maximumProjectionY = getTitle();

newImage("Untitled", "16-bit black", width * downScalingFactorInXY, height * downScalingFactorInXY, 1);
rename("Maximum projection in Z");
maximumProjectionZ = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push data to GPU
Ext.CLIJ_push(input);
Ext.CLIJ_push(downscaled);
Ext.CLIJ_push(maximumProjectionX);
Ext.CLIJ_push(maximumProjectionY);
Ext.CLIJ_push(maximumProjectionZ);

// cleanup ImageJ
run("Close All");

// process
Ext.CLIJ_downsample3d(input, downscaled, downScalingFactorInXY, downScalingFactorInXY, downScalingFactorInZ);

Ext.CLIJ_maxProjectionDimSelect(downscaled, maximumProjectionX, 2, 1, 0);
Ext.CLIJ_maxProjectionDimSelect(downscaled, maximumProjectionY, 2, 0, 1);
Ext.CLIJ_maxProjectionDimSelect(downscaled, maximumProjectionZ, 0, 1, 2);

// show results
Ext.CLIJ_pull(maximumProjectionX);
Ext.CLIJ_pull(maximumProjectionY);
Ext.CLIJ_pull(maximumProjectionZ);
