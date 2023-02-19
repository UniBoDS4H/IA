package com.ds4h.model.cornerManager;

import com.ds4h.model.imageCorners.ImageCorners;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CornerManager {
    private final List<ImageCorners> imagesWithCorners;
    private Optional<ImageCorners> sourceImage;
    public CornerManager(){
        this.sourceImage = Optional.empty();
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
        if(Objects.nonNull(this.sourceImage) && this.sourceImage.isPresent() && this.sourceImage.get().equals(image)) {
            this.imagesWithCorners.removeIf(img -> img.equals(image));
        }
    }

    public void clearList(){
        this.imagesWithCorners.clear();
    }

    public List<ImageCorners> getCornerImages(){
        return new ArrayList<>(this.imagesWithCorners);
    }

    public List<ImageCorners> getImagesToAlign(){
        return this.sourceImage.map(imageCorners -> this.imagesWithCorners.stream().filter(im -> !im.equals(imageCorners)).collect(Collectors.toList())).orElseGet(() -> new LinkedList<>(this.imagesWithCorners));
    }

    public Optional<ImageCorners> getSourceImage(){
        return this.imagesWithCorners.stream().filter(i-> this.sourceImage.equals(i)).findFirst();
    }

    public void setAsSource(ImageCorners image){
        if(this.imagesWithCorners.contains(image)){
            this.sourceImage = Optional.of(image);
        }else{
            throw new IllegalArgumentException("given image was not fount among the loaded");
        }
    }
}
