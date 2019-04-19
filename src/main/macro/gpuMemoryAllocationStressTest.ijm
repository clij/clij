// gpuMemoryAllocationStressTest.ijm
//
// This macro can be used to determine if there is a memory-leak in CLIJ. Use it with care and don't use it
// while other important stuff is ongoing on your computer. 
// It was written on a day when executing it caused GPU crashes and blue screens.
// 
// Author: Robert Haase
// January 2019
// ---------------------------------------------

newImage("Untitled", "16-bit black", 1024, 1024, 256);

for (i = 0; i < 100; i++) {
	run("Absolute of an image on GPU", "cl_device=gfx902 source=Untitled");
	run("Close");
	run("Blur3D on GPU", "cl_device=gfx902 source=Untitled sigmax=2 sigmay=2 sigmaz=2");
	run("Close");
	run("Report about GPU memory usage", "cl_device=gfx902");
}

