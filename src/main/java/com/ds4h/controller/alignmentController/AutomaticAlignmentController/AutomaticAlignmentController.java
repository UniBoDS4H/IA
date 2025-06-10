package com.ds4h.controller.alignmentController.AutomaticAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.ImageManagerController;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.*;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ImageConverter;

import java.awt.image.ColorModel;
import java.util.*;

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
        return alignment.alignedImages();
    }

    /**
     * Release all the images from the heap.
     */
    @Override
    public void releaseImages(){
        this.alignment.clearList();
    }

    /**
     * @see com.ds4h.model.alignment.automatic.pointDetector
     * @see com.ds4h.model.alignment.alignmentAlgorithm
     * Align all the input images using the "detector" selected from the input as Point Detector, the "algorithm" as alignment algorithm. The "pointManager"
     * is where all the images are stored.
     * @param algorithm the alignment algorithm to use.
     * @param detector the Point Detector to use.
     * @param pointManager where all the images are stored.
     * @throws IllegalArgumentException if one of the input is not correct.
     * @throws RuntimeException if during the alignment something happen.
     */
    public void align(final AlignmentAlgorithm algorithm, final Detectors detector, final ImageManagerController pointManager) throws Exception{

        if (!this.alignment.isAlive() && Objects.nonNull(pointManager) && Objects.nonNull(pointManager.getPointManager())) {
            if (pointManager.getPointManager().getPointImages().size() > 1 && detector.getScaling() >= 1) {
                try{
                    this.alignment.alignImages(pointManager.getPointManager(), algorithm,
                            AlignmentEnum.AUTOMATIC,
                            Objects.requireNonNull(detector.pointDetector()),
                            detector.getThresholdFactor(),
                            detector.getScaling());
                }catch (final Exception e){
                    throw e;
                }
            } else {
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
     * Returns the alignment name.
     * @return the alignment name.
     */
    @Override
    public String name() {
        return "AUTOMATIC";
    }

    /**
     * Returns all the aligned images as stack.
     * @return all the aligned images as stack.
     * @throws RuntimeException if something goes wrong (there are no images, etc, etc..).
     */
    @Override
    public ImagePlus getAlignedImagesAsStack() throws RuntimeException{
        if(!this.getAlignedImages().isEmpty()){
            final ImageStack stack = new ImageStack(this.getAlignedImages().get(0).getAlignedImage().getWidth(),
                    this.getAlignedImages().get(0).getAlignedImage().getHeight(), ColorModel.getRGBdefault());
            System.gc();
            final List<AlignedImage> images = this.getAlignedImages();
            //final LUT[] luts = new LUT[images.size()];
            //int index = 0;
            final int bitDepth = images.stream()
                    .min(Comparator.comparingInt(img -> img.getAlignedImage().getBitDepth())).get().getAlignedImage().getBitDepth();
            IJ.log("[ALIGNED STACK] Bit Depth: " + bitDepth);
            for (final AlignedImage image : images) {
                if(!(image.getAlignedImage().getProcessor() instanceof ByteProcessor)) {
                    final ImageConverter imageConverter = new ImageConverter(image.getAlignedImage());
                    imageConverter.convertToGray8();
                    IJ.log("[STACK] " + (image.getAlignedImage().getProcessor() instanceof ByteProcessor));
                }
                //luts[index] = image.getAlignedImage().getProcessor().getLut();
                IJ.log("[NAME] " + image.getName());
                stack.addSlice(image.getName(), image.getAlignedImage().getProcessor());
                //index++;
            }
            try {
                //final CompositeImage composite = new CompositeImage(new ImagePlus("Aligned Stack", stack));
                //composite.setLuts(luts);
                return new ImagePlus("AlignedStack", stack);
            }catch (Exception e){
                throw new RuntimeException("Something went wrong with the creation of the stack.");
            }
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
     * Returns the algorithm.
     * @return the algorithm
     */
    public AlignmentAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Set the algorithm to use.
     * @param algorithm the algorithm to use.
     */
    public void setAlgorithm(final AlignmentAlgorithm algorithm){
        if(Objects.nonNull(algorithm)) {
            this.algorithm = algorithm;
        }
    }

    /**
     * Returns the alignment algorithm.
     * @param algorithmEnum the selected algorithm.
     * @return the alignment algorithm.
     */
    public AlignmentAlgorithm getAlgorithmFromEnum(final AlignmentAlgorithmEnum algorithmEnum){
        switch (algorithmEnum){
            case TRANSLATIONAL:
                return this.translational;
            case PROJECTIVE:
                return this.projective;
            case AFFINE:
                return this.affine;
        }
        throw new IllegalArgumentException("Algorithm not present");
    }
    public Detectors[] getDetectors(){
        return Detectors.values();
    }
}
