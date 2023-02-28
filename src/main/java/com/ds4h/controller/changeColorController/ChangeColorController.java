package com.ds4h.controller.changeColorController;

import com.ds4h.model.util.ChangeImage;
import ij.ImagePlus;

import java.awt.Color;

public class ChangeColorController {

    private ChangeColorController(){

    }

    public static ImagePlus changeColor(final ImagePlus image, final Color color){
        return ChangeImage.changeColor(image, color);
    }

    public static ImagePlus changeContrast(final ImagePlus imagePlus, final float scaleFactor){
        if(Float.compare(scaleFactor, 0.0f) >= 0 && Float.compare(scaleFactor, 2.0f) <= 0) {
            return ChangeImage.changeContrast(imagePlus, scaleFactor);
        }
        return null;
    }
}
