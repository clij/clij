run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");

run("Blobs (25K)");
makeRectangle(24, 46, 51, 50);
run("Duplicate...", "title=A");

input = getTitle();

run("Duplicate...", "title=B");
output = getTitle();

//Ext.CLIJ_help("");

IJ.log("Hello world 0");

//Ext.CLIJ_help("mean");

Ext.CLIJ_push(input);
Ext.CLIJ_push(output);

IJ.log("Hello world 1");
Ext.CLIJ_mean(input, output, 3, 3, 1);


IJ.log("Hello world 2");
Ext.CLIJ_pull(output);


IJ.log("Hello world 3");