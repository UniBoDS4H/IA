package com.ds4h.model.util;

import java.util.Objects;

public class Pair<T, U> {
    final private T first;
    final private U second;

    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }
}