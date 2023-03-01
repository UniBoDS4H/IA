package com.ds4h.model.cornerManager;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.CheckImage;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class CornerManager {
    private final List<ImagePoints> imagesWithPoints;
    private Optional<ImagePoints> sourceImage;
    public CornerManager(){
        this.sourceImage = Optional.empty();
        this.imagesWithPoints = new ArrayList<>();
    }

    /**
     *
     * @param loadingImages
     */
    public void loadImages(final List<String> loadingImages){
        if(Objects.nonNull(loadingImages) && !loadingImages.isEmpty()) {
            loadingImages.stream()
                    .map(File::new)
                    .filter(CheckImage::checkImage)
                    .map(ImagePoints::new)
                    .filter(image -> !this.imagesWithPoints.contains(image))
                    .forEach(this.imagesWithPoints::add);
            if (this.imagesWithPoints.size() > 0) {
                //setting the first image as default
                this.setAsSource(new ImagePoints(new File(loadingImages.get(0))));
            } else {
                throw new IllegalArgumentException("Zero images found");
            }
        }else{
            throw new IllegalArgumentException("There are no input images, please pick some images from a path.");
        }
    }

    /**
     *
     * @param path
     */
    public void loadImage(final String path){
        if(Objects.isNull(path) && !path.isEmpty()) {
            final File file = new File(path);
            if (CheckImage.checkImage(file)) {
                final ImagePoints image = new ImagePoints(file);
                if (!this.imagesWithPoints.contains(image)) {
                    this.imagesWithPoints.add(image);
                }
            }
        }else{
            throw new IllegalArgumentException("You have to insert a valid file for the image, please pick another file.");
        }
    }

    /**
     *
     * @param images
     */
    public void addImages(final List<ImagePoints> images){
        if(Objects.nonNull(images) && images.size() > 0) {
            this.imagesWithPoints.addAll(images);
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
            System.out.println(this.sourceImage);
        }else{
            throw new IllegalArgumentException("The given image was not fount among the loaded or the image input is NULL.");
        }
    }

    public void clearProject(){
        this.sourceImage = Optional.empty();
        this.imagesWithPoints.clear();
    }
}
