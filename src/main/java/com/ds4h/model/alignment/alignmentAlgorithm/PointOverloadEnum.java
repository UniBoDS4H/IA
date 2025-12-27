package com.ds4h.model.alignment.alignmentAlgorithm;

import com.drew.lang.annotations.NotNull;

public enum PointOverloadEnum {
    FIRST_AVAILABLE("First points available"),
    RANSAC("RANSAC"),
    MINIMUM_LAST_SQUARE("Minimum Last Squares" );

    private final String name;

    /**
     *
     * @param name point overload technique.
     */
    PointOverloadEnum(@NotNull final String name) {
        this.name = name;
    }

    /**
     *
     * @return the name of the point overload.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
