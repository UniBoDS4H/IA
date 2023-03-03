package com.ds4h.controller.alignmentController.semiAutomaticController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.semiAutomatic.SemiAutomaticAlignment;
import com.ds4h.model.cornerManager.CornerManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SemiAutomaticController implements AlignmentControllerInterface {
    private final AlignmentAlgorithm semiAutomatic = new SemiAutomaticAlignment();
    public SemiAutomaticController(){

    }
    @Override
    public List<AlignedImage> getAlignedImages() {
        return new LinkedList<>(this.semiAutomatic.alignedImages());
    }

    @Override
    public boolean isAlive() {
        return semiAutomatic.isAlive();
    }

    @Override
    public String name() {
        return "Semi-Automatic algorithm";
    }

    public void align(final CornerController cornerController) {
        if(!this.semiAutomatic.isAlive() && Objects.nonNull(cornerController) && Objects.nonNull(cornerController.getCornerManager())){
            this.semiAutomatic.alignImages(cornerController.getCornerManager());
        }
    }
}
