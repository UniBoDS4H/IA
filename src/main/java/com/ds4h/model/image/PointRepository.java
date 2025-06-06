package com.ds4h.model.image;

import org.opencv.core.Point;

public interface PointRepository {
    void add(Point point);
    Iterable<Point> getPoints();
}
