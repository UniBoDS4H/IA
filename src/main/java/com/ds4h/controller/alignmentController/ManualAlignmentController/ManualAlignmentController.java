package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithm;
import ij.CompositeImage;
import ij.ImagePlus;
import ij.ImageStack;

import java.util.*;
import java.util.stream.Collectors;

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

    public CompositeImage getAlignedImagesAsStack(){
        List<ImagePlus> alignedImages = this.getAlignedImages().stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList());
        if(!alignedImages.isEmpty()) {
            ImageStack stack = new ImageStack(alignedImages.get(0).getWidth(), alignedImages.get(0).getHeight());
            alignedImages.forEach(a->stack.addSlice(a.getTitle(),a.getProcessor()));
            return null;//new ImagePlus("AglignedStack", stack);
        }
        return null;// new ImagePlus("EmptyStack", new ImageStack());
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
     * @param pointManager
     */
    public void align(final AlignmentAlgorithm algorithm, final PointController pointManager){
        if(!this.alignment.isAlive() && Objects.nonNull(pointManager) && Objects.nonNull(pointManager.getCornerManager())) {
            if(pointManager.getCornerManager().getCornerImages().size() > 1) {
                this.alignment.alignImages(pointManager.getCornerManager(), algorithm, AlignmentEnum.MANUAL);
            }else{
                throw new IllegalArgumentException("For the alignment are needed at least TWO images.");
            }
        }
    }
}
