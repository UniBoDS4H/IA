package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.AlignmentAlgorithmEnum;
import com.ds4h.model.alignment.manual.*;
import com.ds4h.model.cornerManager.CornerManager;

import java.util.*;

public class ManualAlignmentController implements AlignmentControllerInterface {
    private final AlignmentAlgorithm perspectiveAlignment;
    private final AlignmentAlgorithm affineAlignment;
    private final AlignmentAlgorithm translativeAlignment;
    private final AlignmentAlgorithm ransacAlignment;
    private final AlignmentAlgorithm leastMedianAlignment;
    private final List<AlignedImage> images;
    private Optional<AlignmentAlgorithmEnum> lastAlgorithm;
    public ManualAlignmentController(){
        this.images = new LinkedList<>();
        this.lastAlgorithm = Optional.empty();
        this.perspectiveAlignment = new PerspectiveAlignment();
        this.affineAlignment = new AffineAlignment();
        this.translativeAlignment = new TranslationAlignment();
        this.ransacAlignment = new RansacAlignment();
        this.leastMedianAlignment = new LeastMedianAlignment();
    }

    @Override
    public List<AlignedImage> getAlignedImages(){
        if(this.lastAlgorithm.isPresent()) {

            switch (this.lastAlgorithm.get()){
                case TRANSLATION:
                    return new LinkedList<>(translativeAlignment.alignedImages());
                case AFFINE:
                    return new LinkedList<>(affineAlignment.alignedImages());
                case PERSPECTIVE:
                    return new LinkedList<>(perspectiveAlignment.alignedImages());
                case RANSAC:
                    return new LinkedList<>(ransacAlignment.alignedImages());
            }
            this.lastAlgorithm = Optional.empty();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAlive() {
        if(this.lastAlgorithm.isPresent()) {
            switch (this.lastAlgorithm.get()) {
                case TRANSLATION:
                    return translativeAlignment.isAlive();
                case AFFINE:
                    return affineAlignment.isAlive();
                case PERSPECTIVE:
                    return perspectiveAlignment.isAlive();
                case RANSAC:
                    return ransacAlignment.isAlive();
            }
        }
        return false;
    }

    /**
     * Align manually the images using the Homography alignment.
     * @param cornerManager for each Image we have its own points
     */

    public void alignImages(final AlignmentAlgorithmEnum alignmentAlgorithm, final CornerController cornerManager) throws IllegalArgumentException{
        switch (alignmentAlgorithm){
            case TRANSLATION:
                this.align(this.translativeAlignment, cornerManager);
                break;
            case AFFINE:
                this.align(this.affineAlignment, cornerManager);
                break;
            case PERSPECTIVE:
                this.align(this.perspectiveAlignment, cornerManager);
                break;
            case RANSAC:
                this.align(this.ransacAlignment, cornerManager);
                break;
        }
        this.lastAlgorithm = Optional.of(alignmentAlgorithm);
        System.out.println(this.lastAlgorithm);
    }

    public void align(final AlignmentAlgorithm algorithm, final CornerController cornerManager){
        if(!algorithm.isAlive() && Objects.nonNull(cornerManager) && Objects.nonNull(cornerManager.getCornerManager())) {
            algorithm.alignImages(cornerManager.getCornerManager());
        }
    }
}
