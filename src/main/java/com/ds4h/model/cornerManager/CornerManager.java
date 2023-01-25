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



    public List<ImagePlus> getImages() {
        return this.images.keySet().stream().collect(Collectors.toList());
    }
}
