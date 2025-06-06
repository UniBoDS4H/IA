package com.ds4h.model.image;

import org.opencv.core.Point;

public interface PointRepository {
    /**
     * Add a new Point inside the repository.
     * @param point new detected point.
     */
    void add(Point point);

    /**
     * Return an iterable of all the point stored inside the repository.
     * @return an iterable of points.
     */
    Iterable<Point> getPoints();
}
