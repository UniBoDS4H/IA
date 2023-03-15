package com.ds4h.model.alignedImage;

import ij.ImagePlus;
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
    private final ImagePlus alignedImage;
    private final Optional<Mat> registrationMatrix;

    /**
     * Constructor of the AlignedImage. An AlignedImage is the result of the Alignment Algorithm.
     * @param registrationMatrix : The registration matrix in order to have a value for the accuracy of the alignment
     * @param image : The aligned image
     */
    public AlignedImage(final Mat registrationMatrix,  final ImagePlus image){
        this.registrationMatrix = Optional.of(registrationMatrix);
        this.alignedImage = image;
    }
    /**
     * Constructor of the AlignedImage. An AlignedImage is the result of the Alignment Algorithm.
     * This constructor is used for the target image because there is no registration matrix
     * @param image : The aligned image
     */
    public AlignedImage(final ImagePlus image){
        this.registrationMatrix = Optional.empty();
        this.alignedImage = image;
    }

    /**
     * This method returns the name of the aligned image
     * @return the name of the image
     */
    public String getName(){
        return this.alignedImage.getTitle();
    }

    /**
     * This method return the registration matrix
     * @return the registration matrix
     */
    public Optional<Mat> getRegistrationMatrix(){
        return this.registrationMatrix;
    }

    /**
     * This method return the matrix of the aligned image
     * @return the matrix of the image
     */
    public Mat getMat(){
        return Imgcodecs.imread(this.alignedImage.getFileInfo().getFilePath());
    }

    /**
     * This method return the result image.
     * @return get the aligned image
     */
    public ImagePlus getAlignedImage(){
        return this.alignedImage;
    }

}
