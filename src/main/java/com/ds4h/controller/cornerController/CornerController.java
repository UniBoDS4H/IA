package com.ds4h.controller.cornerController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.reuse.ReuseSources;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.view.cornerSelectorGUI.MenuItem;
import org.opencv.core.Point;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CornerController {
    CornerManager cornerManager = new CornerManager();
    public void loadImages(final List<String> paths) throws Exception {
        final List<ImagePoints> imagePoints = ImagingConversion.fromPath(paths)
                    .stream().map(ImagePoints::new)
                    .collect(Collectors.toList());
        if(imagePoints.size() > 1) {
            this.cornerManager.addImages(imagePoints);
        }else{
            throw new IllegalArgumentException("You can not upload one single photo.");
        }
    }

    public CornerManager getCornerManager(){
        return this.cornerManager;
    }

    public List<ImagePoints> getCornerImagesImages() {
        return this.cornerManager.getCornerImages();
    }
    public boolean isSource(ImagePoints image){
        return this.cornerManager.getSourceImage().isPresent() && this.cornerManager.getSourceImage().get().equals(image);
    }

    public void changeTarget(final ImagePoints newTarget){
        this.cornerManager.setAsSource(newTarget);
    }

    public void reuseSource(final List<AlignedImage> alignedImages) throws FileNotFoundException {
        ReuseSources.reuseSources(this.cornerManager, alignedImages);
    }

    public boolean isTarget(final ImagePoints image){
        return this.cornerManager.getSourceImage().isPresent() && this.cornerManager.getSourceImage().get().equals(image);
    }

    public void removeImage(final ImagePoints image){
        if(this.cornerManager.getCornerImages().contains(image)){
            this.cornerManager.removeImage(image);
        }
    }

    public boolean copyCorners(List<Point> selectedPoints, ImagePoints img) {
        boolean res = true;
        for (Point p:selectedPoints) {
            if(this.insideImage(p,img)){
                img.addPoint(p);
            }else{
                res = false;
            }
        }
        return res;
    }

    private boolean insideImage(Point p, ImagePoints img) {
        return img.getMatImage().rows() >= p.y && img.getMatImage().cols() >= p.x;
    }

    public MenuItem getMenuItem(ImagePoints image){
        return new MenuItem(this.getCornerImagesImages().indexOf(image)+1, image);
    }

    public void clearProject(){
        this.cornerManager.clearProject();
    }
}
