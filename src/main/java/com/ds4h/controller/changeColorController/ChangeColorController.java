package com.ds4h.controller.changeColorController;

import com.ds4h.model.util.ChangeColor;
import ij.ImagePlus;

import java.awt.Color;

public class ChangeColorController {

    private ChangeColorController(){

    }

    public static ImagePlus changeColor(final ImagePlus image, final Color color){
        return ChangeColor.changeColor(image, color);
    }
}
