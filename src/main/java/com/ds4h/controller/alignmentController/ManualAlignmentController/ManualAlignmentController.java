package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.*;
import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ImageConverter;
import ij.process.LUT;
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
    private final PointOverloadEnum overload;

    public ManualAlignmentController(){
        this.algorithm = this.translational;
        this.alignment = new Alignment();
        this.overload = PointOverloadEnum.FIRST_AVAILABLE;
    }

    /**
     *
     * @return a
     */
    @Override
    public List<AlignedImage> getAlignedImages(){
        return new LinkedList<>(alignment.alignedImages());
    }

    /**
     *
     * @return b
     */
    public ImagePlus getAlignedImagesAsStack(){
        if(!this.getAlignedImages().isEmpty()){
            final ImageStack stack = new ImageStack(this.getAlignedImages().get(0).getAlignedImage().getWidth(), this.getAlignedImages().get(0).getAlignedImage().getHeight(), ColorModel.getRGBdefault());
            System.gc();
            final List<AlignedImage> images = this.getAlignedImages();
            //LUT[] luts = new LUT[images.size()];
            //int index = 0;
            for (final AlignedImage image : images) {
                if(!(image.getAlignedImage().getProcessor() instanceof ByteProcessor)) {
                    final ImageConverter imageConverter = new ImageConverter(image.getAlignedImage());
                    imageConverter.convertToGray8();
                    IJ.log("[STACK] " + (image.getAlignedImage().getProcessor() instanceof ByteProcessor));
                }
                //luts[index] = image.getAlignedImage().getProcessor().getLut();
                stack.addSlice(image.getName(),image.getAlignedImage().getProcessor());
                //index++;
            }
            //final CompositeImage composite = new CompositeImage(new ImagePlus("Aligned_Stack", stack));
            //composite.setLuts(luts);
            return new ImagePlus("Aligned Stack", stack);
        }
        throw new RuntimeException("The detection has failed, the number of points found can not be used with the selected \"Algorithm\".\n" +
                "Please consider to expand the memory (by going to Edit > Options > Memory & Threads)\n" +
                "increase the Threshold Factor and change the \"Algorithm\".");
    }

    /**
     *
     * @return a
     */
    @Override
    public int getStatus() {
        return this.alignment.getStatus();
    }

    /**
     *
     * @return a
     */
    @Override
    public boolean isAlive() {
        return alignment.isAlive();
    }

    /**
     *
     * @return a
     */
    @Override
    public String name() {
        return "MANUAL";
    }

    /**
     *
     * @param algorithm a
     * @param pointManager b
     */
    public void align(final AlignmentAlgorithm algorithm, final PointController pointManager){
        if(!this.alignment.isAlive() && Objects.nonNull(pointManager) && Objects.nonNull(pointManager.getPointManager())) {
            if(pointManager.getPointManager().getCornerImages().size() > 1) {
                this.alignment.alignImages(pointManager.getPointManager(), algorithm, AlignmentEnum.MANUAL);
            }else{
                throw new IllegalArgumentException("For the alignment are needed at least TWO images.");
            }
        }
    }

    /**
     *
     * @return a
     */
    public AlignmentAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    /**
     *
     * @param algorithm a
     */
    public void setAlgorithm(AlignmentAlgorithm algorithm){
        this.algorithm = algorithm;
    }

    /**
     *
     * @param e a
     * @return b
     */
    public AlignmentAlgorithm getAlgorithmFromEnum(final AlignmentAlgorithmEnum e) {
        switch (e){
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
     *
     * @return a
     */
    public PointOverloadEnum getPointOverload() {
        return this.algorithm.getPointOverload();
    }

    /**
     *
     * @param overload a
     */
    public void setPointOverload(PointOverloadEnum overload) {
        this.algorithm.setPointOverload(overload);
    }
}
