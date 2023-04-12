package com.ds4h.model.pointManager;

import com.ds4h.model.imagePoints.ImagePoints;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class PointManager {
    private final List<ImagePoints> imagesWithPoints;
    private ImagePoints targetImage;

    /**
     * Creates the PointManager object.
     */
    public PointManager(){
        this.targetImage = null;
        this.imagesWithPoints = new ArrayList<>();
    }

    /**
     * Add the images inside "imageWithPoints" ArrayList.
     * @param images the images to be added inside "imageWithPoints".
     */
    public void addImages(final List<ImagePoints> images){
        if(Objects.nonNull(images) && images.size() > 0) {
            images.stream()
                    .filter(img -> !this.imagesWithPoints.contains(img))
                    .forEach(this.imagesWithPoints::add);
            this.setAsTarget(images.get(0));
        }
    }

    /**
     * Remove the image from the "imagesWithPoint".
     * @param image the image to remove.
     */
    public void removeImage(final ImagePoints image){
        if(Objects.nonNull(image) && Objects.nonNull(this.targetImage) && !this.targetImage.equals(image)) {
            this.imagesWithPoints.removeIf(img -> img.equals(image));
        }
    }

    /**
     * Remove all the images inside "imagesWithPoints".
     */
    public void clearList(){
        this.imagesWithPoints.clear();
    }

    /**
     * Returns the list of all the images stored inside the PointManager object.
     * @return the list of all the images.
     */
    public List<ImagePoints> getPointImages(){
        return new ArrayList<>(this.imagesWithPoints);
    }

    /**
     * Returns all the images to align stored inside the PointManager object.
     * @return all the iages to align stored inside the PointManager object.
     */
    public List<ImagePoints> getImagesToAlign(){
        return Optional.ofNullable(this.targetImage)
                .map(imageCorners -> this.imagesWithPoints.stream()
                        .filter(im -> !im.equals(imageCorners))
                .collect(Collectors.toList()))
                .orElseGet(() -> new LinkedList<>(this.imagesWithPoints));
    }

    /**
     * Returns the targetImage.
     * @return the targetImage.
     */
    public Optional<ImagePoints> getTargetImage(){
        return Optional.ofNullable(this.targetImage);
    }

    /**
     * Set the new target inside the PointManager.
     * @param image the new target to be added.
     */
    public void setAsTarget(final ImagePoints image){
        if(Objects.nonNull(image) && this.imagesWithPoints.contains(image)){
            this.targetImage = image;
        }else{
            throw new IllegalArgumentException("The given image was not fount among the loaded or the image input is NULL.");
        }
    }

    /**
     * Remove all the images inside the point manager and also remove the targetImage.  
     */
    public void clearProject(){
        this.targetImage = null;
        this.imagesWithPoints.clear();
        System.gc();
    }
}
