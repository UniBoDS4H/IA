package com.ds4h.controller.AlignmentController.ManualAlignmentController;

import com.ds4h.controller.AlignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.manual.HomographyAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import ij.ImagePlus;

import java.util.LinkedList;
import java.util.List;

public class ManualAlignmentController implements AlignmentControllerInterface {
    private final AlignmentAlgorithm homographyAlignment;
    private final List<ImagePlus> images;
    public ManualAlignmentController(){
        this.images = new LinkedList<>();
        this.homographyAlignment = new HomographyAlignment();
    }

    @Override
    public List<ImagePlus> getAlignedImages(){
        return new LinkedList<>(this.images);
    }

    /**
     * Align manually the images using the Homography alignment.
     * @param cornerManager for each Image we have its own points
     * @return : the images aligned to the source image.
     */
    public void homographyAlignment(final CornerManager cornerManager){
        this.images.clear();
        this.images.addAll(this.homographyAlignment.alignImages(cornerManager));
    }
}
