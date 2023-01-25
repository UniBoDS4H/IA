package com.ds4h.model.cornerManager;

import com.ds4h.model.imageCorners.ImageCorners;
import ij.ImagePlus;
import org.opencv.core.Point;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class CornerManager {
    final List<ImageCorners> imagesWithCorners;
    ImageCorners sourceImage;
    public CornerManager(){
        this.imagesWithCorners = new ArrayList<>();
    }

    public void loadImages(List<String> loadingImages){
        //setting the first image as default
        this.sourceImage = new ImageCorners(new File(loadingImages.get(0)));
        loadingImages.forEach(image -> {
            this.imagesWithCorners.add(new ImageCorners(new File(image)));
        });
    }

    public Optional<ImageCorners> getSourceImage(){
        return Optional.of(this.sourceImage);
    }
}
