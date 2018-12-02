// ClearCLIJ example macro: backgroundSubtraction.ijm
//
// This macro shows how background subtraction can be done in the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------

// Get test data
//open("C:/structure/data/t1-head.tif");
run("T1 Head (2.4M, 16-bits)");
input = getTitle();
run("Duplicate...", "title=background duplicate");
background = getTitle();
run("Duplicate...", "title=background_subtracted duplicate");
background_subtracted = getTitle();
run("Duplicate...", "title=maximum_projected");
maximum_projected = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=HD");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);
Ext.CLIJ_push(background);
Ext.CLIJ_push(background_subtracted);
Ext.CLIJ_push(maximum_projected);
// cleanup ImageJ
close();
close();
close();

// Blur in GPU
Ext.CLIJ_blur3d(input, background, 20, 20, 1, 10, 10, 1);

// subtraction from original
Ext.CLIJ_addWeightedPixelwise(input, background, background_subtracted, 1, -1);

// maximum projection
Ext.CLIJ_maxProjection(background_subtracted, maximum_projected);

// Get results back from GPU
Ext.CLIJ_pull(maximum_projected);

// Cleanup by the end
Ext.CLIJ_clear();
