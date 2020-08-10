package net.haesleinhuepf.common;

import net.imglib2.RandomAccessibleInterval;

public class RandomAccessibleIntervalContainer extends AbstractContainer<RandomAccessibleInterval>{
    public RandomAccessibleIntervalContainer(RandomAccessibleInterval managedItem) {
        super(managedItem);
    }

    @Override
    public long getWidth() {
        return managedItem.dimension(0);
    }

    @Override
    public long getHeight() {
        return managedItem.dimension(1);
    }

    @Override
    public long getDepth() {
        return managedItem.dimension(2);
    }
}
