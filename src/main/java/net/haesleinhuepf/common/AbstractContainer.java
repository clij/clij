package net.haesleinhuepf.common;

public abstract class AbstractContainer<T> implements Container<T> {

    protected T managedItem;

    public AbstractContainer(T managedItem) {

        this.managedItem = managedItem;
    }

    public T get() {
        return managedItem;
    }

}
