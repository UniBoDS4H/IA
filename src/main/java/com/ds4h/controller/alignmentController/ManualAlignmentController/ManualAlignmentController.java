package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.ManualAlgorithm;
import com.ds4h.model.alignment.AlignmentAlgorithmEnum;

import java.util.*;

/**
 *
 */
public class ManualAlignmentController implements AlignmentControllerInterface {
    private final ManualAlgorithm alignment;
    private final ManualAlgorithm leastMedianAlignment;
    private final List<AlignedImage> images;
    private Optional<AlignmentAlgorithmEnum> lastAlgorithm;
    public ManualAlignmentController(){
        this.images = new LinkedList<>();
        this.lastAlgorithm = Optional.empty();
        this.alignment = new ManualAlgorithm();
        this.leastMedianAlignment = null;//new LeastMedianAlignment();
    }

    /**
     *
     * @return
     */
    @Override
    public List<AlignedImage> getAlignedImages(){
        if(this.lastAlgorithm.isPresent()) {

            switch (this.lastAlgorithm.get()){
                case TRANSLATIONAL:
                    return new LinkedList<>(alignment.alignedImages());
            }
            this.lastAlgorithm = Optional.empty();
        }
        return Collections.emptyList();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAlive() {
        if(this.lastAlgorithm.isPresent()) {
            switch (this.lastAlgorithm.get()) {
                case TRANSLATIONAL:
                    return alignment.isAlive();
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public String name() {
        return this.lastAlgorithm.map(algorithmEnum -> algorithmEnum + " " + "Algorithm").orElse("");
    }

    /**
     * Align manually the images using the Homography alignment.
     * @param cornerManager for each Image we have its own points
     */

    public void alignImages(final AlignmentAlgorithmEnum alignmentAlgorithm, final PointController cornerManager) throws IllegalArgumentException{
        switch (alignmentAlgorithm){
            case TRANSLATIONAL:
                this.align(this.alignment, cornerManager);
                break;
        }
        this.lastAlgorithm = Optional.of(alignmentAlgorithm);
        System.out.println(this.lastAlgorithm);
    }

    /**
     *
     * @param algorithm
     * @param cornerManager
     */
    public void align(final ManualAlgorithm algorithm, final PointController cornerManager){
        if(!algorithm.isAlive() && Objects.nonNull(cornerManager) && Objects.nonNull(cornerManager.getCornerManager())) {
            algorithm.alignImages(cornerManager.getCornerManager());
        }
    }
}
