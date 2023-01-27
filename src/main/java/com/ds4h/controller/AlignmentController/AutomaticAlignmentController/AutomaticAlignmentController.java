package com.ds4h.controller.AlignmentController.AutomaticAlignmentController;

import com.ds4h.model.alignment.automatic.SurfAlignment;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.ImagePlus;

import java.util.LinkedList;
import java.util.List;

public class AutomaticAlignmentController {
    public AutomaticAlignmentController(){

    }
    public List<ImagePlus> surfAlignment(final CornerManager cornerManager){
        return SurfAlignment.alignImages(cornerManager);
    }
}
