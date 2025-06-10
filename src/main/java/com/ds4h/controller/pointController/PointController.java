package com.ds4h.controller.pointController;

import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.ImageManager;
import com.ds4h.model.image.imagePoints.ImagePoints;
import com.ds4h.model.reuse.ReuseSources;
import com.ds4h.model.util.imageManager.ImagingConversion;
import com.ds4h.model.util.MemoryController;
import org.opencv.core.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class PointController {
    private final ImageManager imageManager;
    private ConvertLutImageEnum convertType;

    /**
     * Constructor for the PointController object.
     */
    public PointController(){
        this.imageManager = new ImageManager();
    }

    /**
     * Load all the selected FILES from the input.
     * @param paths all the selected files. This will be loaded if the files are images.
     * @throws IllegalArgumentException if the selected file is not an image or the user is trying to load one single images where there are none in the project.
     * @throws IOException If the path does not exists.
     */
    public void loadImages(final List<File> paths) throws IllegalArgumentException, IOException {
        final List<ImagePoints> imagePointsList = ImagingConversion.fromPath(paths);
        if(imagePointsList.size() > 1 || (imagePointsList.size() == 1 && this.imageManager.getPointImages().size() > 0)) {
            this.imageManager.setConvertType(this.convertType);
            this.imageManager.addImages(imagePointsList);
            MemoryController.controlMemory(this.imageManager.getPointImages());
        }else {
            throw new IllegalArgumentException("You can not upload one single photo or less.\n" +
                    "Or maybe what you chose is not a image.");
        }
    }

    /**
     * Returns the "pointManager".
     * @return the pointManager.
     */
    public ImageManager getPointManager(){
        return this.imageManager;
    }

    /**
     * Returns all the "pointImages".
     * @return the pointImages.
     */
    public List<ImagePoints> getPointImages() {
        return this.imageManager.getPointImages();
    }

    /**
     * Change the target image stored inside the Point Controller.
     * @param newTarget the new targetImage.
     */
    public void changeTarget(final ImagePoints newTarget){
        if(Objects.nonNull(newTarget)) {
            this.imageManager.setAsTarget(newTarget);
        }
    }

    /**
     * Reuse all the aligned images as new project.
     * @param alignedImages the new aligned images to be used for the new project.
     * @throws OutOfMemoryError if the project is bigger than the free memory.
     */
    public void reuseSource(final List<AlignedImage> alignedImages) throws OutOfMemoryError {
        ReuseSources.reuseSources(this.imageManager, alignedImages);
        System.gc();
    }

    /**
     * Returns "True" if the input image is the target.
     * @param image the image to check.
     * @return true if the input image is the target.
     */
    public boolean isTarget(final ImagePoints image){
        return this.imageManager.getTargetImage().isPresent() && this.imageManager.getTargetImage().get().equals(image);
    }

    /**
     * Remove the input image from the Point Controller.
     * @param image the image to be removed from the point controller.
     */
    public void removeImage(final ImagePoints image){
        if(this.imageManager.getPointImages().contains(image)){
            image.releaseImage();
            this.imageManager.removeImage(image);
        }
    }

    /**
     * Copy all the "selectedPoints" in to the "img".
     * @param selectedPoints the points to be copied.
     * @param img the image where all the points will be saved.
     * @return "True" if the operation went well.
     */
    public boolean copyPoints(final List<Point> selectedPoints, final ImagePoints img) {
        boolean res = true;
        if(selectedPoints.isEmpty()){
            return false;
        }
        for (final Point p:selectedPoints) {
            if(this.insideImage(p,img)){
                img.add(p);
            }else{
                res = false;
            }
        }
        return res;
    }

    private boolean insideImage(final Point p, final ImagePoints img) {
        return img.getRows() >= p.y && img.getCols() >= p.x;
    }

    /**
     * Clear the entire project from all the images.
     */
    public void clearProject(){
        this.imageManager.clearProject();
        System.gc();
    }

    public void setConvertType(final ConvertLutImageEnum convertType) {
        this.convertType = convertType;
    }
}
