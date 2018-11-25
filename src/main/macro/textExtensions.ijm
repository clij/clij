// Get test data
open("C:/structure/data/fly_florence_400.tif");
run("32-bit");
input = getTitle();
run("Duplicate...", "title=Temp duplicate");
temp = getTitle();
run("Duplicate...", "title=Temp2 duplicate");
temp2 = getTitle();
run("Duplicate...", "title=Result");
output = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);
Ext.CLIJ_push(temp);
Ext.CLIJ_push(temp2);
Ext.CLIJ_push(output);
// cleanup ImageJ
run("Close All");

// Blur in GPU
Ext.CLIJ_blur(input, temp, 20, 20, 1, 10, 10, 1);

// subtraction from original
Ext.CLIJ_multiplyScalar(temp, temp2, -1);
Ext.CLIJ_addPixelwise(input, temp2, temp);

// maximum projection
Ext.CLIJ_maxProjection(temp, output, 0, 1, 2);

// Get results back
Ext.CLIJ_pull(output);
