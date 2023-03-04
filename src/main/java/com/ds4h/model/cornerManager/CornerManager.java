package com.ds4h.model.cornerManager;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.CheckImage;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.twelvemonkeys.contrib.tiff.TIFFUtilities;
import ij.ImagePlus;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @param images
     */
    public void addImages(final List<ImagePoints> images){
        if(Objects.nonNull(images) && images.size() > 0) {
            images.stream().filter(img -> !this.imagesWithPoints.contains(img)).forEach(this.imagesWithPoints::add);
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
    }
}
