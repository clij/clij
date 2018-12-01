// Get test data
run("T1 Head (2.4M, 16-bits)");
input = getTitle();
getDimensions(width, height, channels, slices, frames);

// create an emtpy image to put the blurred pixels in
newImage("Untitled", "8-bit black", width, height, slices);
rename("Blurred");
blurred = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);
Ext.CLIJ_push(blurred);


// Local mean filter in GPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	run("Mean 3D...", "x=3 y=3 z=3");
	print("GPU mean filter no " + i + " took " + (getTime() - time));
}

// cleanup ImageJ
run("Close All");

// Local mean filter in GPU
for (i = 1; i <= 10; i++) {
	time = getTime();
	Ext.CLIJ_mean3d(input, blurred, 3, 3, 3);
	print("GPU mean3d filter no " + i + " took " + (getTime() - time));
}

// Get results back from GPU
Ext.CLIJ_pull(blurred);

// Cleanup GPU 
Ext.CLIJ_clear();
