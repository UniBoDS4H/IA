package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.List;
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
     *
     * @param targetImage : the source image used as reference
     * @param imagePoints : the target to align
     * @param targetSize
     * @return : the list of all the images aligned to the source
     * @throws IllegalArgumentException : in case the number of corners is not correct
     */
    @Override
    public Optional<AlignedImage> align(final List<Point> targetImage, final ImagePoints imagePoints, Size targetSize) throws IllegalArgumentException{
        try {
            if(targetImage.size() == REQUIRED_POINTS && imagePoints.numberOfPoints() == REQUIRED_POINTS) {
                //final MatOfPoint2f targetPoints = targetImage.getMatOfPoint();
                //final MatOfPoint2f imageToShiftPoints = imagePoints.getMatOfPoint();
                final Mat H = super.traslationMatrix(imagePoints);
                final Mat warpedMat = new Mat();
                Imgproc.warpAffine(imagePoints.getMatImage(), warpedMat, H, targetSize, Imgproc.INTER_LINEAR, 0, new Scalar(0, 0, 0));
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
        final Mat H = Imgproc.getAffineTransform(new MatOfPoint2f(imageToAlign.getPoints()), new MatOfPoint2f(targetImage.getPoints()));
        super.addMatrix(imageToAlign, H);
        return H;
    }

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H){
        Core.transform(source, destination, H);
    }
}
