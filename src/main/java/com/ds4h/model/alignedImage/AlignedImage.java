package com.ds4h.model.alignedImage;

import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class AlignedImage {
    private final Mat matrix;
    private final ImagePlus alignedImage;
    private final Optional<Mat> registrationMatrix;

    public AlignedImage(final Mat matrix, final Mat registrationMatrix,  final ImagePlus image){
        this.matrix = matrix;
        this.registrationMatrix = Optional.of(registrationMatrix);
        this.alignedImage = image;
    }
    public AlignedImage(final Mat matrix,  final ImagePlus image){
        this.matrix = matrix;
        this.registrationMatrix = Optional.empty();
        this.alignedImage = image;
    }

    public String getName(){
        return this.alignedImage.getTitle();
    }

    public Optional<Mat> getRegistrationMatrix(){
        return this.registrationMatrix;
    }

    public Mat getMat(){
        return this.matrix;
    }

    public ImagePlus getAlignedImage(){
        return this.alignedImage;
    }

}
