package com.ds4h.model.alignedImage;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represent the new image after the alignment. Inside this class we have the new ImagePlus, the Matrix of the ImagePlus
 * (the warped matrix) and the registrationMatrix (H). This class is used after the process of alignment. The registration matrix
 * is an Optional because for the target image we do not calculate the registration matrix.
 */
public class AlignedImage {
    private final ImageProcessor alignedImage;
    private final String name;
    private final Optional<Mat> registrationMatrix;

    /**
     * Constructor of the AlignedImage. An AlignedImage is the result of the Alignment Algorithm.
     * @param registrationMatrix : The registration matrix in order to have a value for the accuracy of the alignment
     * @param image : The aligned image
     */
    public AlignedImage(final Mat registrationMatrix,  final ImageProcessor image, final String name){
        this.alignedImage = image;
        this.name = name;
        this.registrationMatrix = Optional.of(registrationMatrix);
    }
    /**
     * Constructor of the AlignedImage. An AlignedImage is the result of the Alignment Algorithm.
     * This constructor is used for the target image because there is no registration matrix
     * @param image : The aligned image
     */
    public AlignedImage(final ImageProcessor image, final String name){
        this.registrationMatrix = Optional.empty();
        this.alignedImage = image;
        this.name = name;
    }

    /**
     * This method returns the name of the aligned image
     * @return the name of the image
     */
    public String getName(){
        return this.name;
    }

    /**
     * This method return the registration matrix
     * @return the registration matrix
     */
    public Optional<Mat> getRegistrationMatrix(){
        return this.registrationMatrix;
    }
    

    /**
     * This method return the result image.
     * @return get the aligned image
     */
    public ImagePlus getAlignedImage(){
        return new ImagePlus(this.name,  this.alignedImage);
    }

}
