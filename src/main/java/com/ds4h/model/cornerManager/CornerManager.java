package com.ds4h.model.cornerManager;

import ij.ImagePlus;
import org.opencv.core.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CornerManager {
    final Map<ImagePlus, List<Point>> images = new HashMap<>();

    public void loadImages(List<ImagePlus> loadingImages){
        loadingImages.forEach(image -> {
            this.images.put(image, new ArrayList<>());
        });
    }

    public List<Point> getCorners(ImagePlus image){
        return this.images.get(image);
    }

    public void addCorner(ImagePlus image, Point corner){
            List<Point> points =  this.images.containsKey(image)?images.get(image):new ArrayList<>();
            points.add(corner);
            this.images.put(image, points);
    }
    public void removeCorner(ImagePlus image, Point corner){
        if(this.images.containsKey(image)){
            List<Point> points = this.images.get(image);
            points.remove(corner);
            this.images.put(image, points);
        }else{
            throw new IllegalArgumentException("given image was not found");
        }
    }
    public void moveCorner(ImagePlus image, Point corner, Point newCorner){

    }

    public List<ImagePlus> getImages() {
        return this.images.keySet().stream().collect(Collectors.toList());
    }
}
