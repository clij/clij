package net.haesleinhuepf.common;

import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;

public class ImagePlusContainer extends AbstractContainer<ImagePlus>{
    public ImagePlusContainer(ImagePlus managedItem) {
        super(managedItem);
    }

    @Override
    public long getWidth() {
        return managedItem.getWidth();
    }

    @Override
    public long getHeight() {
        return managedItem.getHeight();
    }

    @Override
    public long getDepth() {
        return managedItem.getNSlices();
    }
}
