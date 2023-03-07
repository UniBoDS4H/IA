package com.ds4h.controller.alignmentController.semiAutomaticController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class SemiAutomaticController implements AlignmentControllerInterface {
    private final AlignmentAlgorithm semiAutomatic = null;//new SemiAutomaticAlignment();
    public SemiAutomaticController(){

    }

    /**
     *
     * @return
     */
    @Override
    public List<AlignedImage> getAlignedImages() {
        return new LinkedList<>(this.semiAutomatic.alignedImages());
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAlive() {
        return semiAutomatic.isAlive();
    }

    /**
     *
     * @return
     */
    @Override
    public String name() {
        return "Semi-Automatic algorithm";
    }

    /**
     *
     * @param pointController
     */
    public void align(final PointController pointController) {
        if(!this.semiAutomatic.isAlive() && Objects.nonNull(pointController) && Objects.nonNull(pointController.getCornerManager())){
            this.semiAutomatic.alignImages(pointController.getCornerManager());
        }
    }
}
