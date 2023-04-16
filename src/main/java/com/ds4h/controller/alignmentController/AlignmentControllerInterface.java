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

    /**
     * Returns the aligned images.
     * @return the aligned images.
     */
    public List<AlignedImage> getAlignedImages();

    /**
     * Returns the thread status.
     * @return the thread status.
     */
    public boolean isAlive();

    /**
     * Returns the name of the algorithm.
     * @return the name of the algorithm.
     */
    public String name();

    /**
     * Returns all the aligned images as stack.
     * @return all the aligned images as stack,
     */
    public ImagePlus getAlignedImagesAsStack();

    /**
     * Returns the alignment status.
     * @return the alignment status.
     */
    int getStatus();
}
