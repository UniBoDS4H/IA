package com.ds4h.controller.alignmentController.ManualAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.AlignmentAlgorithmEnum;
import com.ds4h.model.alignment.manual.AffineAlignment;
import com.ds4h.model.alignment.manual.PerspectiveAlignment;
import com.ds4h.model.alignment.manual.TranslativeAlignment;
import com.ds4h.model.cornerManager.CornerManager;

import java.util.LinkedList;
import java.util.List;

public class ManualAlignmentController implements AlignmentControllerInterface {
    private final AlignmentAlgorithm homographyAlignment;
    private final AlignmentAlgorithm affineAlignment;
    private final AlignmentAlgorithm translativeAlignment;
    private final List<AlignedImage> images;
    public ManualAlignmentController(){
        this.images = new LinkedList<>();
        this.homographyAlignment = new PerspectiveAlignment();
        this.affineAlignment = new AffineAlignment();
        this.translativeAlignment = new TranslativeAlignment();
    }

    @Override
    public List<AlignedImage> getAlignedImages(){
        return new LinkedList<>(this.images);
    }

    /**
     * Align manually the images using the Homography alignment.
     * @param cornerManager for each Image we have its own points
     */

    public void alignImages(final AlignmentAlgorithmEnum alignmentAlgorithm, final CornerManager cornerManager){
        switch (alignmentAlgorithm){
            case TRANSLATIVE:
                this.translativeAlignment(cornerManager);
                break;
            case AFFINE:
                this.affineAlignment(cornerManager);
                break;
            case PERSPECTIVE:
                this.homographyAlignment(cornerManager);
                break;
        }
    }
    private void homographyAlignment(final CornerManager cornerManager){
        this.images.clear();
        this.images.addAll(this.homographyAlignment.alignImages(cornerManager));
    }

    private void affineAlignment(final CornerManager cornerManager){
        this.images.clear();
        this.images.addAll(this.affineAlignment.alignImages(cornerManager));
    }

    private void translativeAlignment(final CornerManager cornerManager){
        this.images.clear();
        this.images.addAll(this.translativeAlignment.alignImages(cornerManager));
    }
}
