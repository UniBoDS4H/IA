package com.ds4h.model.pointManager;

import com.ds4h.controller.pointController.ConvertLutImageEnum;
import com.ds4h.model.image.imagePoints.ImagePoints;
import ij.process.ImageConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class has all the images to be aligned stored inside "imagesWithPoints". With the using of this class we can manage all the images
 * stored inside the Plugin, add new images, remove images and so on.
 */
public class ImageManager {
    private final List<ImagePoints> imagesWithPoints;
    @Nullable private ImagePoints targetImage;
    private ConvertLutImageEnum convertType;

    /**
     * Constructor the PointManager object.
     */
    public ImageManager(){
        this.targetImage = null;
        this.imagesWithPoints = new ArrayList<>();
    }

    /**
     * Add the images inside "imageWithPoints" ArrayList.
     * @param images the images to be added inside "imageWithPoints".
     */
    public void addImages(@NotNull final List<ImagePoints> images){
        if(!images.isEmpty()) {
            for (final ImagePoints img : images) {
                if (!this.imagesWithPoints.contains(img)) {
                    if (img.getLuts().length > 0 && (
                            img.getLuts()[0].getRed(255) != 255 ||
                            img.getLuts()[0].getGreen(255) != 255 ||
                            img.getLuts()[0].getBlue(255) != 255
                            )) {
                        switch (this.convertType) {
                            case CONVERT_TO_RGB:
                                new ImageConverter(img).convertToRGB();
                                break;
                            case CONVERT_TO_EIGHT_BIT:
                                new ImageConverter(img).convertToGray8();
                                break;
                        }
                    }
                    this.imagesWithPoints.add(img);
                }
            }
            if (images.get(0).getLuts().length > 0 && (
                    images.get(0).getLuts()[0].getRed(255) != 255 ||
                    images.get(0).getLuts()[0].getGreen(255) != 255 ||
                    images.get(0).getLuts()[0].getBlue(255) != 255
            )) {
                switch (this.convertType) {
                    case CONVERT_TO_RGB:
                        new ImageConverter(images.get(0)).convertToRGB();
                        break;
                    case CONVERT_TO_EIGHT_BIT:
                        new ImageConverter(images.get(0)).convertToGray8();
                        break;
                }
            }
            this.setAsTarget(images.get(0));
        }
    }

    /**
     * Remove the image from the "imagesWithPoint".
     * @param image the image to remove.
     */
    public void removeImage(@NotNull final ImagePoints image){
        if(Objects.nonNull(this.targetImage) && !this.targetImage.equals(image)) {
            this.imagesWithPoints.removeIf(img -> img.equals(image));
            image.close();
            System.gc();
        }
    }

    /**
     * Remove all the images inside "imagesWithPoints".
     */
    public void clearList(){
        this.imagesWithPoints.forEach(ImagePoints::releaseImage);
        this.imagesWithPoints.clear();
    }

    /**
     * Returns the list of all the images stored inside the PointManager object.
     * @return the list of all the images.
     */
    @NotNull
    public List<ImagePoints> getPointImages(){
        return new ArrayList<>(this.imagesWithPoints);
    }

    /**
     * Returns all the images to align stored inside the PointManager object.
     * @return all the images to align stored inside the PointManager object.
     */
    @NotNull
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
    @NotNull
    public Optional<ImagePoints> getTargetImage(){
        return Optional.ofNullable(this.targetImage);
    }

    /**
     * Set the new target inside the PointManager.
     * @param image the new target to be added.
     */
    public void setAsTarget(@NotNull final ImagePoints image){
        if(this.imagesWithPoints.contains(image)){
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
        this.clearList();
    }

    public void setConvertType(@NotNull final ConvertLutImageEnum convertType) {
        this.convertType = convertType;
    }
}
