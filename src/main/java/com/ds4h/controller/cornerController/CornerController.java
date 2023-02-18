package com.ds4h.controller.cornerController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.reuse.ReuseSources;

import java.io.FileNotFoundException;
import java.util.List;

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

    public void changeTarget(ImageCorners newTarget){
        this.cornerManager.setAsSource(newTarget);
    }

    public void reuseSource(final List<AlignedImage> alignedImages) throws FileNotFoundException {
        final List<ImageCorners> images = ReuseSources.reuseSources(this.cornerManager, alignedImages);
        if(!images.isEmpty()) {
            this.cornerManager.clearList();
            this.cornerManager.addImages(images);
        }
    }
}
