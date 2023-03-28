package com.ds4h.controller.alignmentController.AutomaticAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.Alignment;
import com.ds4h.model.alignment.AlignmentEnum;
import com.ds4h.model.alignment.alignmentAlgorithm.AlignmentAlgorithm;
import com.ds4h.model.alignment.alignmentAlgorithm.TranslationalAlignment;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageWindow;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;
import ij.process.LUT;

import java.awt.*;
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
    private final AlignmentAlgorithm traslational = new TranslationalAlignment();

    /**
     * Constructor of the Controller
     */
    public AutomaticAlignmentController(){
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
    public void align(final Detectors detector, final PointController pointManager){
        if(!this.alignment.isAlive() && Objects.nonNull(pointManager) && Objects.nonNull(pointManager.getCornerManager())) {
            if(pointManager.getCornerManager().getCornerImages().size() > 1) {
                this.alignment.alignImages(pointManager.getCornerManager(), this.traslational,
                        AlignmentEnum.AUTOMATIC,
                        Objects.requireNonNull(detector.pointDetector()),
                        detector.getFactor());
            }else{
                throw new IllegalArgumentException("For the alignment are needed at least TWO images.");
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

    @Override
    public String name() {
        return "AUTOMATIC";
    }

    @Override
    public CompositeImage getAlignedImagesAsStack() {
        if(!this.getAlignedImages().isEmpty()){
            ImageStack stack = new ImageStack(this.getAlignedImages().get(0).getAlignedImage().getWidth(), this.getAlignedImages().get(0).getAlignedImage().getHeight(), ColorModel.getRGBdefault());
            System.gc();
            List<AlignedImage> images = this.getAlignedImages();
            LUT[] luts = new LUT[images.size()];
            int index = 0;
            for (AlignedImage image : images) {
                luts[index] = image.getAlignedImage().getProcessor().getLut();
                stack.addSlice(image.getAlignedImage().getProcessor());
                index++;
            }
            CompositeImage composite = new CompositeImage(new ImagePlus("AglignedStack", stack));
            composite.setLuts(luts);
            return composite;
        }
        throw new RuntimeException("stack is empty");
    }
}
