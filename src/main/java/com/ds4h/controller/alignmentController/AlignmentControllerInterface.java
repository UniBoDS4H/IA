package com.ds4h.controller.alignmentController;

import com.ds4h.model.alignedImage.AlignedImage;

import java.util.List;

/**
 *
 */
public interface AlignmentControllerInterface {

    public List<AlignedImage> getAlignedImages();

    public boolean isAlive();

    public String name();
}
