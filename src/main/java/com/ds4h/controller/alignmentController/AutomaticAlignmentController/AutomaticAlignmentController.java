package com.ds4h.controller.alignmentController.AutomaticAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.AbstractAutomaticAlignment;
import com.ds4h.model.alignment.automatic.AutomaticAlgorithm;
import com.ds4h.model.alignment.automatic.pointDetector.surfDetector.SURFDetector;
import com.ds4h.model.alignment.manual.TranslationalAlignment;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class is used in order to call all the Model methods of the SURF Alignment inside the view, without
 * using the exact class model because we want to use the MVC Pattern.
 */
public class AutomaticAlignmentController implements AlignmentControllerInterface {
    private final AbstractAutomaticAlignment surfAlignment;
    private final List<AlignedImage> alignedImages;
    private final List<AlignedImage> deformedImages;

    /**
     * Constructor of the Controller
     */
    public AutomaticAlignmentController(){
        this.surfAlignment = new AutomaticAlgorithm(new SURFDetector(), new TranslationalAlignment());
        this.alignedImages = new LinkedList<>();
        this.deformedImages = new LinkedList<>();
    }

    /**
     * When this method is called, we check if the alignment is already running, if it is so, we do nothing otherwise
     * if the thread is not alive it means that the alignment algorithm is not running, we can start the alignment thread.
     * @param cornerManager in order to get all the images to align
     * @throws IllegalArgumentException if some of ImagePoints are not correct, an exception is thrown.
     * if the alignment algorithm we do nothing
     */
    public void surfAlignment(final PointController cornerManager) throws IllegalArgumentException{
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

    @Override
    public String name() {
        return "SURF Algorithm";
    }

}
