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

    public void loadImages(final List<String> loadingImages){
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
        //TODO: if the source is not set we must take the first image inside the list and use it as Source.
        //TODO: I do not think that is wise return an Optional. It only will be helpfull if there is no images at all inside this container
        return Optional.of(this.sourceImage);
    }

    public void setAsSource(final ImageCorners image){
        /*
        this.imagesWithCorners.forEach(im->
            System.out::println
        );
        */
        System.out.println(image);
        if(this.imagesWithCorners.contains(image)){
            //TODO:remove the source image from the list
            this.sourceImage = image;
        }else{
            throw new IllegalArgumentException("given image was not fount among the loaded");
        }
    }
}
