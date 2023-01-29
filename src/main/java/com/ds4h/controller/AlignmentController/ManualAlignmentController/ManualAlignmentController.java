package com.ds4h.controller.AlignmentController.ManualAlignmentController;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.manual.HomographyAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import ij.ImagePlus;

import java.util.Collections;
import java.util.List;

public class ManualAlignmentController {
    final AlignmentAlgorithm homographyAlignment;
    public ManualAlignmentController(){
        this.homographyAlignment = new HomographyAlignment();
    }

    /**
     * Align manually the images using the Homography alignment.
     * @param cornerManager for each Image we have its own points
     * @return : the images aligned to the source image.
     */
    public List<ImagePlus> homographyAlignment(final CornerManager cornerManager){
        return this.homographyAlignment.alignImages(cornerManager);
    }
}
