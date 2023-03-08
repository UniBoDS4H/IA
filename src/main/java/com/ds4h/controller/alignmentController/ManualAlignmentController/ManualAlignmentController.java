package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithm;

import java.util.*;

/**
 *
 */
public class ManualAlignmentController implements AlignmentControllerInterface {
    private final Alignment alignment;
    public ManualAlignmentController(){
        this.alignment = new Alignment();
    }

    /**
     *
     * @return
     */
    @Override
    public List<AlignedImage> getAlignedImages(){
        return new LinkedList<>(alignment.alignedImages());
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAlive() {
        return alignment.isAlive();
    }

    /**
     *
     * @return
     */
    @Override
    public String name() {
        return "MANUAL";
    }

    /**
     *
     * @param algorithm
     * @param cornerManager
     */
    public void align(final AlignmentAlgorithm algorithm, final PointController cornerManager){
        if(!this.alignment.isAlive() && Objects.nonNull(cornerManager) && Objects.nonNull(cornerManager.getCornerManager())) {
            this.alignment.alignImages(cornerManager.getCornerManager(), algorithm, AlignmentEnum.MANUAL);
        }
    }
}
