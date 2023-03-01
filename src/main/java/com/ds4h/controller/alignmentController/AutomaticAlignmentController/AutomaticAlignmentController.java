package com.ds4h.controller.alignmentController.AutomaticAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.cornerController.CornerController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.Pair;
import ij.ImagePlus;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class is used in order to call all the Model methods of the SURF Alignment inside the view, without
 * using the exact class model because we want to use the MVC Pattern.
 */
public class AutomaticAlignmentController implements AlignmentControllerInterface {
    final AlignmentAlgorithm surfAlignment;
    final List<AlignedImage> alignedImages;

    /**
     * Constructor of the Controller
     */
    public AutomaticAlignmentController(){
        this.surfAlignment = new SurfAlignment();
        this.alignedImages = new LinkedList<>();
    }

    /**
     * When this method is called, we check if the alignment is already running, if it is so, we do nothing otherwise
     * if the thread is not alive it means that the alignment algorithm is not running, we can start the alignment thread.
     * @param cornerManager in order to get all the images to align
     * @throws IllegalArgumentException if some of ImagePoints are not correct, an exception is thrown.
     * if the alignment algorithm we do nothing
     */
    public void surfAlignment(final CornerController cornerManager) throws IllegalArgumentException{
        if(!this.isAlive() && Objects.nonNull(cornerManager) && Objects.nonNull(cornerManager.getCornerManager())) {
            this.surfAlignment.alignImages(cornerManager.getCornerManager());
        }
    }

    /**
     * When this method is called we get from the Alignment Algorithm all the images aligned. Be careful because maybe
     * the collection can be empty if the alignment algorithm is not done.
     * @return the list with all the images aligned.
     */
    @Override
    public List<AlignedImage> getAlignedImages() {
        return new LinkedList<>(this.surfAlignment.alignedImages());
    }

    /**
     * This method is used in order to get all the infos about the running thread, if it still alive it means
     * the alignment algorithm is not done yet, otherwise the alignment is done.
     * @return true if the alignment algorithm is running, false otherwise
     */
    @Override
    public boolean isAlive(){
        return this.surfAlignment.isAlive();
    }

}
