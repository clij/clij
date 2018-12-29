run("Close All");

open("C:/structure/data/t1-head.tif");
run("CLIJ Macro Extensions", "cl_device=[Intel(R) HD Graphics 400]");
Ext.CLIJ_push("t1-head.tif");
Ext.CLIJ_resliceLeft("t1-head.tif", "CLIJ_resliceLeft_destination_t1-head.tif");
Ext.CLIJ_rotateRight("CLIJ_resliceLeft_destination_t1-head.tif", "CLIJ_rotateRight_destination_CLIJ_resliceLeft_destination_t1-head.tif");
Ext.CLIJ_pull("CLIJ_rotateRight_destination_CLIJ_resliceLeft_destination_t1-head.tif");
Ext.CLIJ_reportMemory();