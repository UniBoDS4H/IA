package com.ds4h.controller.alignmentController;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.image.alignedImage.AlignedImage;
import ij.ImagePlus;
import org.bytedeco.opencv.presets.opencv_core;

import java.util.List;

/**
 *
 */
public interface AlignmentControllerInterface {

    /**
     * Returns the aligned images.
     * @return the aligned images.
     */
    @NotNull
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
    @NotNull
    public String name();

    /**
     * Returns all the aligned images as stack.
     * @return all the aligned images as stack,
     */
    @NotNull
    public ImagePlus getAlignedImagesAsStack();

    /**
     * Returns the alignment status.
     * @return the alignment status.
     */
    int getStatus();

    void releaseImages();
}
