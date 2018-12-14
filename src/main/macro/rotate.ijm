// ClearCLIJ example macro: rotate.ijm
//
// This macro shows how stacks can be rotated in the GPU
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------


// Get test data
run("T1 Head (2.4M, 16-bits)");
//open("C:/structure/data/t1-head.tif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

newImage("Untitled", "16-bit black", height, width, slices);
rename("Rotate left");
rotated = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push data to GPU
Ext.CLIJ_push(input);
Ext.CLIJ_push(rotated);

// cleanup ImageJ
run("Close All");

// rotate
Ext.CLIJ_rotateLeft(input, rotated);

// show results
Ext.CLIJ_pull(rotated);
