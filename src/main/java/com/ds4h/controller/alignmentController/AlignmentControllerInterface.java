package com.ds4h.controller.alignmentController;

import com.ds4h.model.alignedImage.AlignedImage;
import ij.CompositeImage;
import ij.ImagePlus;

import java.awt.*;
import java.util.List;

/**
 *
 */
public interface AlignmentControllerInterface {

    public List<AlignedImage> getAlignedImages();

    public boolean isAlive();

    public String name();
    public CompositeImage getAlignedImagesAsStack();

    int getStatus();
}
