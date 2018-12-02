// Get test data
run("T1 Head (2.4M, 16-bits)");
input = getTitle();
getDimensions(width, height, channels, slices, frames);

// create an emtpy image to put the blurred pixels in
newImage("Untitled", "16-bit black", width, height, slices);
rename("Blurred");
blurred = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);
Ext.CLIJ_push(blurred);


// Local mean filter in CPU
selectWindow(input);
run("Mean 3D...", "x=3 y=3 z=3");
selectWindow(blurred);

// cleanup ImageJ
close();

// Local mean filter in GPU
Ext.CLIJ_mean3d(input, blurred, 3, 3, 3);

// Get results back from GPU
Ext.CLIJ_pull(blurred);

// Cleanup GPU 
Ext.CLIJ_clear();
