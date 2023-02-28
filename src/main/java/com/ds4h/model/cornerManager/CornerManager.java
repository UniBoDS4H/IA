package com.ds4h.model.cornerManager;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CheckImage;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class CornerManager {
    private final List<ImageCorners> imagesWithCorners;
    private Optional<ImageCorners> sourceImage;
    public CornerManager(){
        this.sourceImage = Optional.empty();
        this.imagesWithCorners = new ArrayList<>();
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
                    .map(ImageCorners::new)
                    .filter(image -> !this.imagesWithCorners.contains(image))
                    .forEach(this.imagesWithCorners::add);
            if (this.imagesWithCorners.size() > 0) {
                //setting the first image as default
                this.setAsSource(new ImageCorners(new File(loadingImages.get(0))));
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
                final ImageCorners image = new ImageCorners(file);
                if (!this.imagesWithCorners.contains(image)) {
                    this.imagesWithCorners.add(image);
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
    public void addImages(final List<ImageCorners> images){
        if(Objects.nonNull(images) && images.size() > 0) {
            this.imagesWithCorners.addAll(images);
            this.setAsSource(images.get(0));
        }
    }

    /**
     *
     * @param image
     */
    public void removeImage(final ImageCorners image){
        if(Objects.nonNull(image) && this.sourceImage.isPresent() && !this.sourceImage.get().equals(image)) {
            this.imagesWithCorners.removeIf(img -> img.equals(image));
        }
    }

    /**
     *
     */
    public void clearList(){
        this.imagesWithCorners.clear();
    }

    /**
     *
     * @return
     */
    public List<ImageCorners> getCornerImages(){
        return new ArrayList<>(this.imagesWithCorners);
    }

    /**
     *
     * @return
     */
    public List<ImageCorners> getImagesToAlign(){
        return this.sourceImage.map(imageCorners -> this.imagesWithCorners.stream().filter(im -> !im.equals(imageCorners)).collect(Collectors.toList())).orElseGet(() -> new LinkedList<>(this.imagesWithCorners));
    }

    /**
     *
     * @return
     */
    public Optional<ImageCorners> getSourceImage(){
        return this.sourceImage;
    }

    /**
     *
     * @param image
     */
    public void setAsSource(final ImageCorners image){
        if(Objects.nonNull(image) && this.imagesWithCorners.contains(image)){
            this.sourceImage = Optional.of(image);
            System.out.println(this.sourceImage);
        }else{
            throw new IllegalArgumentException("The given image was not fount among the loaded or the image input is NULL.");
        }
    }
}
