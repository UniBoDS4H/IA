package com.ds4h.model.image;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Point;

public interface PointRepository {
    /**
     * Add a new Point inside the repository.
     * @param point new detected point.
     */
    void add(@NotNull Point point);

    /**
     * Return an iterable of all the point stored inside the repository.
     * @return an iterable of points.
     */
    @NotNull
    Iterable<Point> getPoints();

    /**
     * Return the total amount of point that are stored inside the repository.
     * @return the amount of points stored.
     */
    Integer totalPoints();
}
