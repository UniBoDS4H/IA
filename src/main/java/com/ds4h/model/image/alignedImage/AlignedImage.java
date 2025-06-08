package com.ds4h.model.image.alignedImage;

import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.DataImage;
import com.ds4h.model.image.PointRepository;
import com.ds4h.model.util.imageManager.ImageProcessorMatConverter;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represent the new image after the alignment. Inside this class we have the new ImagePlus, the Matrix of the ImagePlus
 * (the warped matrix) and the registrationMatrix (H). This class is used after the process of alignment. The registration matrix
 * is an Optional because for the target image we do not calculate the registration matrix.
 */
public class AlignedImage implements AnalyzableImage {
    private final List<Point> points;
    private final ImageProcessor alignedImage;
    private final String name;
    private final Mat registrationMatrix;

    /**
     * Constructor of the AlignedImage. An AlignedImage is the result of the Alignment Algorithm.
     * @param registrationMatrix : The registration matrix in order to have a value for the accuracy of the alignment
     * @param image : The aligned image
     */
    public AlignedImage(final Mat registrationMatrix,  final ImageProcessor image, final String name){
        this.alignedImage = image;
        this.name = name;
        this.registrationMatrix = registrationMatrix;
        this.points = new LinkedList<>();
    }

    /**
     * Constructor of the AlignedImage. An AlignedImage is the result of the Alignment Algorithm.
     * This constructor is used for the target image because there is no registration matrix
     * @param image : The aligned image
     */
    public AlignedImage(final ImageProcessor image, final String name){
        this.registrationMatrix = null;
        this.alignedImage = image;
        this.name = name;
        this.points = new LinkedList<>();
    }

    /**
     * This method returns the name of the aligned image
     * @return the name of the image
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the ImageProcessor.
     * @return the ImageProcessor.
     */
    public ImageProcessor getProcessor(){
        return this.alignedImage;
    }

    /**
     * This method return the registration matrix
     * @return the registration matrix
     */
    public Optional<Mat> getRegistrationMatrix(){
        return Optional.ofNullable(this.registrationMatrix);
    }
    

    /**
     * This method return the result image.
     * @return get the aligned image
     */
    public ImagePlus getAlignedImage(){
        return new ImagePlus(this.name,  this.alignedImage);
    }

    /**
     * Returns the toString of the object.
     * @return the toString of the object.
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Returns if two Aligned Images are equals.
     * @param o the other AlignedImage .
     * @return "True" if are equals.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AlignedImage that = (AlignedImage) o;
        return Objects.equals(alignedImage, that.alignedImage) && Objects.equals(registrationMatrix, that.registrationMatrix);
    }

    /**
     * Returns the hashCode.
     * @return the hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(alignedImage, registrationMatrix);
    }

    /**
     * Release the Matrix if is present. Remove the original ImageProcessor and remove all the points from the Image.
     */
    public void releaseImage(){
        if(Objects.nonNull(this.registrationMatrix)){
            registrationMatrix.release();
        }
        this.getAlignedImage().setProcessor(new ByteProcessor(1,1));
        this.getAlignedImage().close();
    }

    @NotNull
    @Override
    public Mat getGrayScaleMat() {return ImageProcessorMatConverter.convertGray(this.getProcessor());}

    @Override
    public void improve() {}

    @Override
    public boolean toImprove() {return false;}

    @Override
    public void add(@NotNull Point point) {
        this.points.add(point);
    }

    @NotNull
    @Override
    public Iterable<Point> getPoints() {
        return this.points;
    }

    @Override
    public Integer totalPoints() {
        return this.points.size();
    }

    @Override
    public void clear() {
        this.points.clear();
    }
}
