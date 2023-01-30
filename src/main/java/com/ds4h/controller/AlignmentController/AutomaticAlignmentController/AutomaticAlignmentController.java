package com.ds4h.controller.AlignmentController.AutomaticAlignmentController;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.AlignmentTest;
import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.ImagePlus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AutomaticAlignmentController {
    final AlignmentAlgorithm surfAlignment = new SurfAlignment();
    public AutomaticAlignmentController(){

    }
    public List<ImagePlus> surfAlignment(final CornerManager cornerManager){
        //ImageCorners i = cornerManager.getImagesToAlign().get(0);
        //AlignmentTest.Aling(cornerManager.getSourceImage().get(), i);
        //return Collections.emptyList();
        return this.surfAlignment.alignImages(cornerManager);
    }
}
