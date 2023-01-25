package com.ds4h.controller.cornerController;

import com.ds4h.model.cornerManager.CornerManager;
import ij.ImagePlus;
import ij.io.Opener;

import java.util.List;
import java.util.stream.Collectors;

public class CornerController {
    CornerManager cornerManager = new CornerManager();
    public void loadImages(List<String> paths){
        this.cornerManager.loadImages(paths);
    }

    public List<ImagePlus> getImages() {
        return this.cornerManager.getImages();
    }
}
