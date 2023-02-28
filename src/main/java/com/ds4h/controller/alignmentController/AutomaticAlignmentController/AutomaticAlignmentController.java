package com.ds4h.controller.alignmentController.AutomaticAlignmentController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.Pair;
import ij.ImagePlus;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AutomaticAlignmentController implements AlignmentControllerInterface {
    final AlignmentAlgorithm surfAlignment;
    final List<AlignedImage> alignedImages;
    public AutomaticAlignmentController(){
        this.surfAlignment = new SurfAlignment();
        this.alignedImages = new LinkedList<>();
    }
    public void surfAlignment(final CornerManager cornerManager) throws IllegalArgumentException{
        if(!this.isAlive()) {
            this.surfAlignment.alignImages(cornerManager);
        }
    }

    @Override
    public List<AlignedImage> getAlignedImages() {
        return new LinkedList<>(this.surfAlignment.alignedImages());
    }

    @Override
    public boolean isAlive(){
        return this.surfAlignment.isAlive();
    }
}
