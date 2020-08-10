package net.haesleinhuepf.common;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class ClearCLBufferContainer extends AbstractContainer<ClearCLBuffer> {
    public ClearCLBufferContainer(ClearCLBuffer managedItem) {
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
        return managedItem.getDepth();
    }
}
