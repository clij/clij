run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");

run("Blobs (25K)");
input = getTitle();

run("Duplicate...", " ");
output = getTitle();

//Ext.CLIJ_help("");

IJ.log("Hello world 0");

//Ext.CLIJ_help("mean");

Ext.CLIJ_push(input);
Ext.CLIJ_push(output);


IJ.log("Hello world 1");
Ext.CLIJ_mean(input, output, 3, 3);


IJ.log("Hello world 2");
Ext.CLIJ_push(output);


IJ.log("Hello world 3");