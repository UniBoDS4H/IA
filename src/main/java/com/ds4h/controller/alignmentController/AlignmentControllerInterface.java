package com.ds4h.controller.alignmentController;

import com.ds4h.model.alignedImage.AlignedImage;
import ij.ImagePlus;

import java.util.List;

public interface AlignmentControllerInterface {

    public List<AlignedImage> getAlignedImages();
}
