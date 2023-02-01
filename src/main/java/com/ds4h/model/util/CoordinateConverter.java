package com.ds4h.model.util;

import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.Objects;
import java.util.Optional;

public class CoordinateConverter {
    public static Point getMatIndexFromPoint(Point p, int rows, int cols, int width, int height){
        int r = (int)p.y * rows / height;
        int c = (int)p.x * cols / width;
        return new Point(c,r);
    }

    public static Point getPointFromMatIndex(Point p, int rows, int cols, int width, int height){
        int x = (int)p.x * width / cols;
        int y = (int)p.y * height / rows;
        return new Point(x+1,y+1);
    }
}
