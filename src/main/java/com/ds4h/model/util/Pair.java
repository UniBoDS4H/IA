package com.ds4h.model.util;

import java.util.Objects;

/**
 * This class represent a Pair of two objects.
 * @param <T> first object.
 * @param <U> second object.
 */
public class Pair<T, U> {
    final private T first;
    final private U second;

    /**
     * Constructor for the creation of a Pair object.
     * @param first the first object.
     * @param second the seconds object.
     */
    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first object.
     * @return the first object.
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the second object.
     * @return the second object.
     */
    public U getSecond() {
        return second;
    }

    /**
     * Returns the hashcode of the Pair.
     * @return the hashcode of the Pair.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }
}