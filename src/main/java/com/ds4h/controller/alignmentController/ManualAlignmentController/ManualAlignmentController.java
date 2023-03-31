package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.*;
import ij.CompositeImage;
import ij.ImagePlus;
import ij.ImageStack;
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

    public ManualAlignmentController(){
        this.algorithm = this.translational;
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
        if(!this.getAlignedImages().isEmpty()){
            ImageStack stack = new ImageStack(this.getAlignedImages().get(0).getAlignedImage().getWidth(), this.getAlignedImages().get(0).getAlignedImage().getHeight(), ColorModel.getRGBdefault());
            System.gc();
            List<AlignedImage> images = this.getAlignedImages();
            LUT[] luts = new LUT[images.size()];
            int index = 0;
            for (AlignedImage image : images) {
                luts[index] = image.getAlignedImage().getProcessor().getLut();
                stack.addSlice(image.getName(),image.getAlignedImage().getProcessor());
                index++;
            }
            CompositeImage composite = new CompositeImage(new ImagePlus("Aligned_Stack", stack));
            composite.setLuts(luts);
            return composite;
        }
        throw new RuntimeException("stack is empty");
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

    public AlignmentAlgorithm getAlgorithm() {
        return this.algorithm;
    }
    public void setAlgorithm(AlignmentAlgorithm algorithm){
        this.algorithm = algorithm;
    }

    public AlignmentAlgorithm getAlgorithmFromEnum(AlignmentAlgorithmEnum e) {
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
}
