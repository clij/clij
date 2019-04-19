// CLIJ example macro: binaryProcessingBenchmarking.ijm
//
// This macro compares performance of 4-connected (sphere)
// and 8-connected (box) neighborhood-based filters for 
// binary image processing.
//
// Author: Robert Haase
// April 2019
// ---------------------------------------------

run("Close All");


// Get test data
newImage("Untitled", "8-bit noise", 2048, 2048, 100);
getDimensions(width, height, channels, slices, frames);
input = getTitle();
threshold = 128;

mask = "mask";

temp1 = "temp1";
temp2 = "temp2";



// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push data to GPU
Ext.CLIJ_push(input);
rename(temp1);
Ext.CLIJ_push(temp1);
rename(temp2);
Ext.CLIJ_push(temp2);


// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJ_thresholdIJ(input, mask, threshold);

// erosion benchmarking
for (i = 0; i < 5; i++) {
	time = getTime();
	Ext.CLIJ_erodeBoxIJ(mask, temp1);
	IJ.log("8-connected erosion took: " + (getTime() - time));
}

for (i = 0; i < 5; i++) {
	time = getTime();
	Ext.CLIJ_erodeSphere(mask, temp2);
	IJ.log("4-connected erosion took: " + (getTime() - time));
}

// minimum benchmarking
radius = 5;
for (i = 0; i < 5; i++) {
	time = getTime();
	Ext.CLIJ_minimum3DBox(mask, temp1, radius, radius, radius);
	IJ.log("box minimum took: " + (getTime() - time));
}

for (i = 0; i < 5; i++) {
	time = getTime();
	Ext.CLIJ_minimum3DSphere(mask, temp1, radius, radius, radius);
	IJ.log("sphere minimum took: " + (getTime() - time));
}



// show result
Ext.CLIJ_pull(mask);


