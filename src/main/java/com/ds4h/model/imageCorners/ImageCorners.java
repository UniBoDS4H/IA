package com.ds4h.model.imageCorners;
import ij.ImagePlus;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

public class ImageCorners {
    private final ImagePlus image;
    private final List<Point> corners;

    public ImageCorners(ImagePlus image){
        this.image = image;
        this.corners = new ArrayList<>();
    }

    public List<Point> getCorners(ImagePlus image){
        return new ArrayList<>(this.corners);
    }

    public void addCorner(Point corner){
        if(!this.corners.contains(corner)){
            this.corners.add(corner);
        }
    }
    public void removeCorner(Point corner){
        if(!this.corners.remove(corner)){
            throw new IllegalArgumentException("given corner was not found");
        }
    }
    public void moveCorner(ImagePlus image, org.opencv.core.Point corner, org.opencv.core.Point newCorner){

    }
}
