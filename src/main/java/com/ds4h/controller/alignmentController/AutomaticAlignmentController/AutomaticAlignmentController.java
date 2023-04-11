package com.ds4h.controller.alignmentController.AutomaticAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.*;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.LUT;
import java.awt.image.ColorModel;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class is used in order to call all the Model methods of the SURF Alignment inside the view, without
 * using the exact class model because we want to use the MVC Pattern.
 */
public class AutomaticAlignmentController implements AlignmentControllerInterface {
    private final Alignment alignment;
    private AlignmentAlgorithm algorithm;
    private final AlignmentAlgorithm translational = new TranslationalAlignment();
    private final AlignmentAlgorithm projective = new ProjectiveAlignment();
    private final AlignmentAlgorithm affine = new AffineAlignment();

    /**
     * Constructor of the Controller
     */
    public AutomaticAlignmentController(){
        this.algorithm = this.translational;
        this.alignment = new Alignment();
    }


    /**
     * When this method is called we get from the Alignment Algorithm all the images aligned. Be careful because maybe
     * the collection can be empty if the alignment algorithm is not done.
     * @return the list with all the images aligned.
     */
    @Override
    public List<AlignedImage> getAlignedImages() {
        return new LinkedList<>(alignment.alignedImages());
    }

    /**
     *
     * @param algorithm a
     * @param detector b
     * @param pointManager c
     * @throws IllegalArgumentException d
     * @throws RuntimeException e
     */
    public void align(final AlignmentAlgorithm algorithm, final Detectors detector, final PointController pointManager) throws IllegalArgumentException, RuntimeException{
        if(!this.alignment.isAlive() && Objects.nonNull(pointManager) && Objects.nonNull(pointManager.getPointManager())) {
            if(pointManager.getPointManager().getCornerImages().size() > 1 && detector.getScaling() >= 1) {
                this.alignment.alignImages(pointManager.getPointManager(), algorithm,
                        AlignmentEnum.AUTOMATIC,
                        Objects.requireNonNull(detector.pointDetector()),
                        detector.getFactor(),
                        detector.getScaling());
            }else{
                throw new IllegalArgumentException("For the alignment are needed at least TWO images and the SCALING FACTOR must be at least 1.");
            }
        }
    }

    /**
     * This method is used in order to get all the infos about the running thread, if it still alive it means
     * the alignment algorithm is not done yet, otherwise the alignment is done.
     * @return true if the alignment algorithm is running, false otherwise
     */
    @Override
    public boolean isAlive(){
        return this.alignment.isAlive();
    }

    /**
     *
     * @return a
     */
    @Override
    public String name() {
        return "AUTOMATIC";
    }

    /**
     *
     * @return a
     * @throws RuntimeException b
     */
    @Override
    public CompositeImage getAlignedImagesAsStack() throws RuntimeException{
        if(!this.getAlignedImages().isEmpty()){
            final ImageStack stack = new ImageStack(this.getAlignedImages().get(0).getAlignedImage().getWidth(),
                    this.getAlignedImages().get(0).getAlignedImage().getHeight(), ColorModel.getRGBdefault());
            System.gc();
            final List<AlignedImage> images = this.getAlignedImages();
            final LUT[] luts = new LUT[images.size()];
            int index = 0;
            for (final AlignedImage image : images) {
                luts[index] = image.getAlignedImage().getProcessor().getLut();
                IJ.log("[NAME] " + image.getName());
                stack.addSlice(image.getName(), image.getAlignedImage().getProcessor());
                index++;
            }
            try {
                //new ImagePlus("Aligned Stack", stack).show();
                final CompositeImage composite = new CompositeImage(new ImagePlus("Aligned Stack", stack));
                composite.setLuts(luts);
                return composite;
            }catch (Exception e){
                throw new RuntimeException("Something went wrong with the creation of the stack.");
            }
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
    public AlignmentAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    /**
     *
     * @param algorithm a
     */
    public void setAlgorithm(final AlignmentAlgorithm algorithm){
        this.algorithm = algorithm;
    }

    /**
     *
     * @param e a
     * @return b
     */
    public AlignmentAlgorithm getAlgorithmFromEnum(final AlignmentAlgorithmEnum e){
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