package com.ds4h.controller.AlignmentController.ManualAlignmentController;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.manual.HomographyAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import ij.ImagePlus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ManualAlignmentController {
    private final AlignmentAlgorithm homographyAlignment;
    private final List<ImagePlus> images;
    public ManualAlignmentController(){
        this.images = new LinkedList<>();
        this.homographyAlignment = new HomographyAlignment();
    }

    public List<ImagePlus> getAlignedImages(){
        return this.images;
    }

    /**
     * Align manually the images using the Homography alignment.
     * @param cornerManager for each Image we have its own points
     * @return : the images aligned to the source image.
     */
    public List<ImagePlus> homographyAlignment(final CornerManager cornerManager){
        this.images.clear();
        this.images.addAll(this.homographyAlignment.alignImages(cornerManager));
        this.images.add(cornerManager.getSourceImage().get().getImage());
        return this.images;
    }
}
