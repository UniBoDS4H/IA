package com.ds4h.model.imageCorners;
import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import ij.io.Opener;
import org.opencv.core.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
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
        //TODO : Check if the Optional is present. If it so then you return it otherwise it gives you error if the Optional is empty.
        //TODO: maybe it is better return the Optional.
        return img.get();
    }

    public String getPath(){
        return this.image.getPath();
    }

    public Point[] getCorners(){
        return this.corners.toArray(new Point[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageCorners that = (ImageCorners) o;
        return Objects.equals(image, that.image) && Objects.equals(corners, that.corners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, corners);
    }

    public void addCorner(Point corner){
        if(!this.corners.contains(corner)){
            this.corners.add(corner);
        }
    }

    public File getFile(){
        return this.image;
    }
    public void removeCorner(Point corner){
        if(!this.corners.remove(corner)){
            throw new IllegalArgumentException("given corner was not found");
        }
    }
    public void moveCorner(File image, Point corner, Point newCorner){

    }
}
