package com.ds4h.model.imageCorners;
import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import ij.io.Opener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.util.Optional;
import java.util.stream.Collectors;

public class ImageCorners {
    private final File image;
    private final List<Point> corners;

    public ImageCorners(File image){
        this.image = image;
        this.corners = new ArrayList<>();
    }
    public ImagePlus getImage(){
        Optional<ImagePlus> img = ImagingConversion.fromSinglePathToImagePlus(this.image.getPath());
        return img.get();
    }

    public String getPath(){
        return this.image.getPath();
    }

    public List<Point> getCorners(){
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
    public void moveCorner(File image, Point corner, Point newCorner){

    }
}
