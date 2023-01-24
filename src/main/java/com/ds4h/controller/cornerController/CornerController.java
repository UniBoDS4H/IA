package com.ds4h.controller.cornerController;

import com.ds4h.model.cornerManager.CornerManager;
import ij.ImagePlus;
import ij.io.Opener;

import java.util.List;
import java.util.stream.Collectors;

public class CornerController {
    CornerManager cornerManager = new CornerManager();
    public void loadImages(List<String> paths){
        Opener opener = new Opener();
        this.cornerManager.loadImages(paths.stream().map(path -> opener.openImage(path)).collect(Collectors.toList()));
    }

    public List<ImagePlus> getImages() {
        return this.cornerManager.getImages();
    }
}
