package com.ds4h.model.pointManager;

import com.ds4h.model.imagePoints.ImagePoints;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class PointManager {
    private final List<ImagePoints> imagesWithPoints;
    private Optional<ImagePoints> sourceImage;
    public PointManager(){
        this.sourceImage = Optional.empty();
        this.imagesWithPoints = new ArrayList<>();
    }

    /**
     *
     * @param images
     */
    public void addImages(final List<ImagePoints> images){
        if(Objects.nonNull(images) && images.size() > 0) {
            images.stream()
                    .filter(img -> !this.imagesWithPoints.contains(img))
                    .forEach(this.imagesWithPoints::add);
            this.setAsSource(images.get(0));
        }
    }

    /**
     *
     * @param image
     */
    public void removeImage(final ImagePoints image){
        if(Objects.nonNull(image) && this.sourceImage.isPresent() && !this.sourceImage.get().equals(image)) {
            this.imagesWithPoints.removeIf(img -> img.equals(image));
        }
    }

    /**
     *
     */
    public void clearList(){
        this.imagesWithPoints.clear();
    }

    /**
     *
     * @return
     */
    public List<ImagePoints> getCornerImages(){
        return new ArrayList<>(this.imagesWithPoints);
    }

    /**
     *
     * @return
     */
    public List<ImagePoints> getImagesToAlign(){
        return this.sourceImage.map(imageCorners -> this.imagesWithPoints.stream().filter(im -> !im.equals(imageCorners)).collect(Collectors.toList())).orElseGet(() -> new LinkedList<>(this.imagesWithPoints));
    }

    /**
     *
     * @return
     */
    public Optional<ImagePoints> getSourceImage(){
        return this.sourceImage;
    }

    /**
     *
     * @param image
     */
    public void setAsSource(final ImagePoints image){
        if(Objects.nonNull(image) && this.imagesWithPoints.contains(image)){
            this.sourceImage = Optional.of(image);
        }else{
            throw new IllegalArgumentException("The given image was not fount among the loaded or the image input is NULL.");
        }
    }

    public void clearProject(){
        this.sourceImage = Optional.empty();
        this.imagesWithPoints.clear();
        System.gc();
    }
}
