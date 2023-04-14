package com.ds4h.controller.pointController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.reuse.ReuseSources;
import com.ds4h.model.util.imageManager.ImagingConversion;
import com.ds4h.model.util.MemoryController;
import com.ds4h.view.pointSelectorGUI.MenuItem;
import org.opencv.core.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class PointController {
    private final PointManager pointManager;

    /**
     *
     */
    public PointController(){
        this.pointManager = new PointManager();
    }

    /**
     *
     * @param paths a
     * @throws IllegalArgumentException b
     * @throws IOException c
     */
    public void loadImages(final List<File> paths) throws IllegalArgumentException, IOException {
        final List<ImagePoints> imagePointsList = ImagingConversion.fromPath(paths);
        System.gc();
        if(imagePointsList.size() > 1 || (imagePointsList.size() == 1 && this.pointManager.getPointImages().size() > 0)) {
            this.pointManager.addImages(imagePointsList);
            MemoryController.controlMemory(this.pointManager.getPointImages());
        }else {
            throw new IllegalArgumentException("You can not upload one single photo or less.\n" +
                    "Or maybe what you chose is not a image.");
        }
    }

    /**
     *
     * @return a
     */
    public PointManager getPointManager(){
        return this.pointManager;
    }

    /**
     *
     * @return a
     */
    public List<ImagePoints> getCornerImagesImages() {
        return this.pointManager.getPointImages();
    }

    /**
     *
     * @param image a
     * @return b
     */
    public boolean isSource(final ImagePoints image){
        return this.pointManager.getTargetImage().isPresent() && this.pointManager.getTargetImage().get().equals(image);
    }

    /**
     *
     * @param newTarget a
     */
    public void changeTarget(final ImagePoints newTarget){
        this.pointManager.setAsTarget(newTarget);
    }

    /**
     *
     * @param alignedImages a
     * @throws FileNotFoundException b
     * @throws OutOfMemoryError c
     */
    public void reuseSource(final List<AlignedImage> alignedImages) throws FileNotFoundException, OutOfMemoryError {
        ReuseSources.reuseSources(this.pointManager, alignedImages);
    }

    /**
     *
     * @param image a
     * @return b
     */
    public boolean isTarget(final ImagePoints image){
        return this.pointManager.getTargetImage().isPresent() && this.pointManager.getTargetImage().get().equals(image);
    }

    /**
     *
     * @param image a
     */
    public void removeImage(final ImagePoints image){
        if(this.pointManager.getPointImages().contains(image)){
            this.pointManager.removeImage(image);
        }
    }

    /**
     *
     * @param selectedPoints a
     * @param img b
     * @return c
     */
    public boolean copyPoints(final List<Point> selectedPoints, final ImagePoints img) {
        boolean res = true;
        for (final Point p:selectedPoints) {
            if(this.insideImage(p,img)){
                img.addPoint(p);
            }else{
                res = false;
            }
        }
        return res;
    }

    /**
     *
     * @param p a
     * @param img b
     * @return c
     */
    private boolean insideImage(final Point p, final ImagePoints img) {
        return img.getRows() >= p.y && img.getCols() >= p.x;
    }


    public MenuItem getMenuItem(final ImagePoints image){
        //TODO: non penso vada bene, non ci deve essere nulla della view per MVC
        return new MenuItem(this.getCornerImagesImages().indexOf(image)+1, image);
    }

    /**
     *
     */
    public void clearProject(){
        this.pointManager.getImagesToAlign().forEach(image -> image = null);
        this.pointManager.clearProject();
        System.gc();
    }
}
