package com.ds4h.controller.alignmentController.semiAutomaticController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.semiAutomatic.SemiAutomaticAlignment;
import com.ds4h.model.cornerManager.CornerManager;

import java.util.LinkedList;
import java.util.List;

public class SemiAutomaticController implements AlignmentControllerInterface {
    private final AlignmentAlgorithm semiAutomatic = new SemiAutomaticAlignment();
    private final List<AlignedImage> images = new LinkedList<>();
    public SemiAutomaticController(){

    }

    public void align(final CornerManager cornerManager) throws IllegalArgumentException{
        this.images.clear();
        this.images.addAll(semiAutomatic.alignImages(cornerManager));
    }

    @Override
    public List<AlignedImage> getAlignedImages() {
        return new LinkedList<>(this.images);
    }

    @Override
    public boolean isAlive() {
        return false;
    }
}
