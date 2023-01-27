package com.ds4h.controller.cornerController;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.ImagePlus;
import ij.io.Opener;

import java.util.List;
import java.util.stream.Collectors;

public class CornerController {
    CornerManager cornerManager = new CornerManager();
    public void loadImages(List<String> paths){
        this.cornerManager.loadImages(paths);
    }

    public List<ImageCorners> getCornerImagesImages() {
        return this.cornerManager.getCornerImagesImages();
    }
    public boolean isSource(ImageCorners image){
        return this.cornerManager.getSourceImage().isPresent() && this.cornerManager.getSourceImage().get().equals(image);
    }

    public void changeTarget(ImageCorners newTarget){
        this.cornerManager.setAsSource(newTarget);
    }
}
