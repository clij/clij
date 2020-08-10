package net.haesleinhuepf.common;

public interface Container <T> {
    T get();
    long getWidth();
    long getHeight();
    long getDepth();
}
