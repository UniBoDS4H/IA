package com.ds4h.model.alignedImage;

import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.io.File;
import java.util.Objects;

public class AlignedImage {
    private final Mat matrix;
    private final ImagePlus alignedImage;

    public AlignedImage(final Mat matrix,  final ImagePlus image){
        this.matrix = matrix;
        this.alignedImage = image;
    }

    public Mat getMat(){
        return this.matrix;
    }

    public ImagePlus getAlignedImage(){
        return this.alignedImage;
    }

}
