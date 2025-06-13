package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.ImageManagerController;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.*;
import com.ds4h.view.util.ImageStackCreator;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ImageConverter;

import java.awt.image.ColorModel;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class ManualAlignmentController implements AlignmentControllerInterface {
    private final Alignment alignment;
    private AlignmentAlgorithm algorithm;
    private final AlignmentAlgorithm translational = new TranslationalAlignment();
    private final AlignmentAlgorithm projective = new ProjectiveAlignment();
    private final AlignmentAlgorithm affine = new AffineAlignment();

    /**
     * Constructor for the ManualAlignmentController object.
     */
    public ManualAlignmentController(){
        this.algorithm = this.translational;
        this.alignment = new Alignment();
    }

    /**
     * Returns all the aligned images.
     * @return all the aligned images.
     */
    @Override
    public List<AlignedImage> getAlignedImages(){
        return new LinkedList<>(alignment.alignedImages());
    }

    /**
     * Returns all the aligned images as stack.
     * @return all the aligned images as stack.
     */
    public ImagePlus getAlignedImagesAsStack(){
        if(!this.getAlignedImages().isEmpty()){
            return ImageStackCreator.createImageStack(this.getAlignedImages());
        }
        throw new RuntimeException("The detection has failed, the number of points found can not be used with the selected \"Algorithm\".\n" +
                "Please consider to expand the memory (by going to Edit > Options > Memory & Threads)\n" +
                "increase the Threshold Factor and change the \"Algorithm\".");
    }

    /**
     * Returns the alignment status.
     * @return the alignment status.
     */
    @Override
    public int getStatus() {
        return this.alignment.getStatus();
    }

    /**
     * Return the thread status.
     * @return the thread status.
     */
    @Override
    public boolean isAlive() {
        return alignment.isAlive();
    }

    /**
     * Returns the algorithm name.
     * @return the algorithm name.
     */
    @Override
    public String name() {
        return "MANUAL";
    }

    /**
     * @see com.ds4h.model.alignment.alignmentAlgorithm
     * Align all the images with the selected algorithm.
     * @param algorithm the algorithm to use.
     * @param pointManager where all the images are stored.
     * @throws IllegalArgumentException if one of the parameters is not correct.
     */
    public void align(final AlignmentAlgorithm algorithm, final ImageManagerController pointManager) throws IllegalArgumentException{
        if(!this.alignment.isAlive() && Objects.nonNull(pointManager) && Objects.nonNull(pointManager.getImageManager())) {
            if(pointManager.getImageManager().getPointImages().size() > 1) {
                this.alignment.alignImages(pointManager.getImageManager(), algorithm, AlignmentEnum.MANUAL);
            }else{
                throw new IllegalArgumentException("For the alignment are needed at least TWO images.");
            }
        }
    }

    /**
     * Returns the algorithm to use.
     * @return the algorithm to use.
     */
    public AlignmentAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Set the algorithm for the alignment.
     * @param algorithm the algorithm for the alignment.
     */
    public void setAlgorithm(final AlignmentAlgorithm algorithm){
        if(Objects.nonNull(algorithm)) {
            this.algorithm = algorithm;
        }
    }

    /**
     * Returns the algorithm type.
     * @param algorithmEnum selected algorithm.
     * @return the algorithm type.
     */
    public AlignmentAlgorithm getAlgorithmFromEnum(final AlignmentAlgorithmEnum algorithmEnum) {

        switch (Objects.requireNonNull(algorithmEnum)){
            case TRANSLATIONAL:
                return this.translational;
            case PROJECTIVE:
                return this.projective;
            case AFFINE:
                return this.affine;
        }
        throw new IllegalArgumentException("Algorithm not present");
    }

    /**
     * Returns the point overload.
     * @return the point overload.
     */
    public PointOverloadEnum getPointOverload() {
        return this.algorithm.getPointOverload();
    }

    /**
     * Set the point overload from the input value.
     * @param overload the new overload that will be used.
     */
    public void setPointOverload(final PointOverloadEnum overload) {
        if(Objects.nonNull(overload)) {
            this.algorithm.setPointOverload(overload);
        }
    }

    @Override
    public void releaseImages(){
        this.alignment.clearList();
    }
}
