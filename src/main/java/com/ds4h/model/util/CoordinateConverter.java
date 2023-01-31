package com.ds4h.model.util;

import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.Objects;
import java.util.Optional;

public class CoordinateConverter {
    public static Optional<Point> convertPoint(final ImagePlus imp, final Mat mat, final int x, final int y) {
        Optional<Point> point;
        if(Objects.nonNull(imp) && Objects.nonNull(mat)) {
            final int y_mat = mat.rows() - y - 1;
            return Optional.of(new Point(x, y_mat));
        }else{
            return Optional.empty();
        }
    }
}
