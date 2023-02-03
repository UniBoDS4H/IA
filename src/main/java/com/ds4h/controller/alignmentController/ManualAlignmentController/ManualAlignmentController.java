package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.manual.HomographyAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.Pair;
import ij.ImagePlus;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ManualAlignmentController implements AlignmentControllerInterface {
    private final AlignmentAlgorithm homographyAlignment;
    private final List<AlignedImage> images;
    public ManualAlignmentController(){
        this.images = new LinkedList<>();
        this.homographyAlignment = new HomographyAlignment();
    }

    @Override
    public List<AlignedImage> getAlignedImages(){
        return new LinkedList<>(this.images);
    }

    /**
     * Align manually the images using the Homography alignment.
     * @param cornerManager for each Image we have its own points
     */
    public void homographyAlignment(final CornerManager cornerManager){
        this.images.clear();
        this.images.addAll(this.homographyAlignment.alignImages(cornerManager));
    }
}
