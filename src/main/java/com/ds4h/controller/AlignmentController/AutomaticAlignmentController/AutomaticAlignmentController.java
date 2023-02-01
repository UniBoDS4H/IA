package com.ds4h.controller.AlignmentController.AutomaticAlignmentController;

import com.ds4h.controller.AlignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.AlignmentTest;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.ImagePlus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AutomaticAlignmentController implements AlignmentControllerInterface {
    final AlignmentAlgorithm surfAlignment;
    final List<ImagePlus> alignedImages;
    public AutomaticAlignmentController(){
        this.surfAlignment = new SurfAlignment();
        this.alignedImages = new LinkedList<>();
    }
    public void surfAlignment(final CornerManager cornerManager){
        this.alignedImages.clear();
        this.alignedImages.addAll(this.surfAlignment.alignImages(cornerManager));
    }

    @Override
    public List<ImagePlus> getAlignedImages() {
        return new LinkedList<>(this.alignedImages);
    }
}
