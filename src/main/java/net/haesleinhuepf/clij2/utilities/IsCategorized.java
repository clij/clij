package net.haesleinhuepf.clij2.utilities;

public interface IsCategorized {
    default boolean isInCategory(String category) {
        return getCategories().toLowerCase().contains(category.toLowerCase());
    }

    String getCategories();
}
