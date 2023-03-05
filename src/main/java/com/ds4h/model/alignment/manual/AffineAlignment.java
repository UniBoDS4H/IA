package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

/**
 * This class is used for the manual alignment using the Affine technique
 */
public class AffineAlignment extends AlignmentAlgorithm {
    public static final int REQUIRED_POINTS = 3;
    public AffineAlignment(){
        super();
    }

    /**
     * Manual alignment using the Affine alignment
     * @param targetImage : the source image used as reference
     * @param  imagePoints : the target to align
     * @throws IllegalArgumentException : in case the number of corners is not correct
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imagePoints) throws IllegalArgumentException{
        try {
            if(targetImage.numberOfPoints() == REQUIRED_POINTS && imagePoints.numberOfPoints() == REQUIRED_POINTS) {
                //final MatOfPoint2f targetPoints = targetImage.getMatOfPoint();
                //final MatOfPoint2f imageToShiftPoints = imagePoints.getMatOfPoint();
                final Mat H = this.getTransformationMatrix(imagePoints, targetImage);
                final Mat warpedMat = new Mat();
                Imgproc.warpAffine(imagePoints.getMatImage(), warpedMat, H, targetImage.getMatImage().size(), Imgproc.INTER_LINEAR, 0, new Scalar(0, 0, 0));
                final Optional<ImagePlus> finalImage = this.convertToImage(imagePoints.getFile(), warpedMat);
                return finalImage.map(imagePlus -> new AlignedImage(warpedMat, H, imagePlus));
            }else{
                throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                        "In order to use the Affine alignment you must use: " + AffineAlignment.REQUIRED_POINTS + " points.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public Mat getTransformationMatrix(final ImagePoints imageToAlign, final ImagePoints targetImage) {
        return Imgproc.getAffineTransform(new MatOfPoint2f(imageToAlign.getPoints()), new MatOfPoint2f(targetImage.getPoints()));
    }

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H){
        Core.transform(source, destination, H);
    }
}
