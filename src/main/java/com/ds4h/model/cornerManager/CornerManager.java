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
        loadingImages.forEach(image -> {
            this.imagesWithCorners.add(new ImageCorners(new File(image)));
        });
        //setting the first image as default
        this.setAsSource(new ImageCorners(new File(loadingImages.get(0))));
    }

    public List<ImageCorners> getCornerImagesImages(){
        return new ArrayList<>(this.imagesWithCorners);
    }

    public Optional<ImageCorners> getSourceImage(){
        return Optional.of(this.sourceImage);
    }

    public void setAsSource(ImageCorners image){
        this.imagesWithCorners.forEach(im->{
            System.out.println(im);
        });
        System.out.println(image);
        if(this.imagesWithCorners.contains(image)){
            this.sourceImage = image;
        }else{
            throw new IllegalArgumentException("given image was not fount among the loaded");
        }
    }
}
