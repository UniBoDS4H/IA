package com.ds4h.model.alignment.alignmentAlgorithm;

public enum PointOverloadEnum {
    FIRST_AVAILABLE("First points available"),
    RANSAC("RANSAC"),
    MINIMUM_LAST_SQUARE("Minimum Last Squares" );
    private final String name;

    /**
     *
     * @param name a
     */
    PointOverloadEnum(final String name) {
        this.name = name;
    }

    /**
     *
     * @return a
     */
    @Override
    public String toString() {
        return this.name;
    }
}
