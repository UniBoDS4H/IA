package com.ds4h.controller.cornerController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.reuse.ReuseSources;
import org.opencv.core.Point;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CornerController {
    CornerManager cornerManager = new CornerManager();
    public void loadImages(List<String> paths){
        this.cornerManager.loadImages(paths);
    }

    public CornerManager getCornerManager(){
        return this.cornerManager;
    }

    public List<ImageCorners> getCornerImagesImages() {
        return this.cornerManager.getCornerImages();
    }
    public boolean isSource(ImageCorners image){
        return this.cornerManager.getSourceImage().isPresent() && this.cornerManager.getSourceImage().get().equals(image);
    }

    public void changeTarget(final ImageCorners newTarget){
        this.cornerManager.setAsSource(newTarget);
    }

    public void reuseSource(final List<AlignedImage> alignedImages) throws FileNotFoundException {
        ReuseSources.reuseSources(this.cornerManager, alignedImages);
    }

    public boolean isTarget(final ImageCorners image){
        return this.cornerManager.getSourceImage().isPresent() && this.cornerManager.getSourceImage().get().equals(image);
    }

    public void removeImage(final ImageCorners image){
        if(this.cornerManager.getCornerImages().contains(image)){
            this.cornerManager.removeImage(image);
        }
    }

    public boolean copyCorners(List<Point> selectedPoints, ImageCorners img) {
        boolean res = true;
        for (Point p:selectedPoints) {
            if(this.insideImage(p,img)){
                img.addCorner(p);
            }else{
                res = false;
            }
        }
        return res;
    }

    private boolean insideImage(Point p, ImageCorners img) {
        return img.getMatImage().rows() >= p.y && img.getMatImage().cols() >= p.x;
    }
}
