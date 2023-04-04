package com.ds4h.model.alignment.alignmentAlgorithm;

public enum PointOverloadEnum {
    FIRST_AVAILABLE("First points available", ""),
    RANSAC("Ransac", ""),
    MINIMUM_LAST_SQUARE("Minimum Last Square", "");
    private final String name;
    private final String description;
    PointOverloadEnum(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
