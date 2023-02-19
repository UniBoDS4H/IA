package com.ds4h.model.cornerManager;

import com.ds4h.model.imageCorners.ImageCorners;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CornerManager {
    private final List<ImageCorners> imagesWithCorners;
    private ImageCorners sourceImage;
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

    public void addImages(final List<ImageCorners> images){
        if(images.size() > 0) {
            this.imagesWithCorners.addAll(images);
            this.setAsSource(images.get(0));
        }
    }

    public void removeImage(final ImageCorners image){
        this.imagesWithCorners.removeIf(img -> img.equals(image));
    }

    public void clearList(){
        this.imagesWithCorners.clear();
    }

    public List<ImageCorners> getCornerImages(){
        return new ArrayList<>(this.imagesWithCorners);
    }

    public List<ImageCorners> getImagesToAlign(){
        return this.imagesWithCorners.stream().filter(im -> !im.equals(this.sourceImage)).collect(Collectors.toList());
    }

    public Optional<ImageCorners> getSourceImage(){
        return this.imagesWithCorners.stream().filter(i-> this.sourceImage.equals(i)).findFirst();
    }

    public void setAsSource(ImageCorners image){
        if(this.imagesWithCorners.contains(image)){
            this.sourceImage = image;
        }else{
            throw new IllegalArgumentException("given image was not fount among the loaded");
        }
    }
}
