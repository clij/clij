// CLIJ example macro: rotateOverwriteOriginal.ijm
//
// This macro shows how to rotate an image in the GPU.
//
// Author: Robert Haase
// January 2019
// ---------------------------------------------


//run("Close All");

// Get test data
//run("Blobs (25K)");
open("C:/structure/data/blobs.gif");


angle_step = 1;

run("32-bit");
rename("original");

getDimensions(width, height, channels, depth, frames);

// reserve the right amount of memory for the result image
newImage("target", "32-bit black", width, height, 360 / angle_step);

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push("original");
Ext.CLIJ_push("target");

// cleanup imagej
run("Close All");
	
count = 0;
for (angle = 0; angle < 360; angle += angle_step) {
	Ext.CLIJ_rotate2D("original", "rotated", angle_step, true);

	// never overwrite the original with the rotated image!
	// the is just an academic example to show what can go wrong
	Ext.CLIJ_copy("rotated", "original");

	// put the rotated image in the right place in the result stack
	Ext.CLIJ_copySlice("rotated", "target", count);
	
	count++;
}

// show result
Ext.CLIJ_pull("target");
run("Invert LUT");
