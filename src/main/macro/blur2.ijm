run("Close All");

//run("Blobs (25K)");
open("C:/structure/data/blobs.gif");

run("Invert LUT");
input = getTitle();

run("CLIJ2 Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();


Ext.CLIJ2_push(input);
Ext.CLIJ2_gaussianBlur3D(input, blurred, 5, 0, 0);
Ext.CLIJ2_pull(blurred);
Ext.CLIJ2_release(blurred);

Ext.Imglib2_push(blurred);
Ext.Imglib2_gaussianBlur(blurred, blurred2, 0, 5, 0);
Ext.Imglib2_pull(blurred2);
