package com.ds4h.model.pointManager;

import com.ds4h.model.imagePoints.ImagePoints;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class PointManager {
    private final List<ImagePoints> imagesWithPoints;
    private ImagePoints sourceImage;
    public PointManager(){
        this.sourceImage = null;
        this.imagesWithPoints = new ArrayList<>();
    }

    /**
     *
     * @param images a
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
     * @param image a
     */
    public void removeImage(final ImagePoints image){
        if(Objects.nonNull(image) && Objects.nonNull(this.sourceImage) && !this.sourceImage.equals(image)) {
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
     * @return a
     */
    public List<ImagePoints> getCornerImages(){
        return new ArrayList<>(this.imagesWithPoints);
    }

    /**
     *
     * @return a
     */
    public List<ImagePoints> getImagesToAlign(){
        final Optional<ImagePoints> target = Optional.ofNullable(this.sourceImage);
        return target
                .map(imageCorners -> this.imagesWithPoints.stream().filter(im -> !im.equals(imageCorners))
                .collect(Collectors.toList())).orElseGet(() -> new LinkedList<>(this.imagesWithPoints));
    }

    /**
     *
     * @return a
     */
    public Optional<ImagePoints> getSourceImage(){
        return Optional.ofNullable(this.sourceImage);
    }

    /**
     *
     * @param image a
     */
    public void setAsSource(final ImagePoints image){
        if(Objects.nonNull(image) && this.imagesWithPoints.contains(image)){
            this.sourceImage = image;
        }else{
            throw new IllegalArgumentException("The given image was not fount among the loaded or the image input is NULL.");
        }
    }

    public void clearProject(){
        this.sourceImage = null;
        this.imagesWithPoints.clear();
        System.gc();
    }
}
