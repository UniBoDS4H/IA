package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

/**
 * This class is used for the manual alignment using the Perspective technique
 */

public class PerspectiveAlignment extends AlignmentAlgorithm {
    public static final int LOWER_BOUND = 4;

    public PerspectiveAlignment(){
        super();
    }

    /**
     * Manual alignment using the Homography alignment
     * @param targetImage : the source image used as reference
     * @param  imagePoints : the target to align
     * @throws IllegalArgumentException : in case the number of corners is not correct
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imagePoints) throws IllegalArgumentException{
        try {
            if(targetImage.numberOfPoints() >= LOWER_BOUND && imagePoints.numberOfPoints() >= LOWER_BOUND) {
                final MatOfPoint2f referencePoint = targetImage.getMatOfPoint();
                final MatOfPoint2f targetPoint = imagePoints.getMatOfPoint();
                //final Mat H = Imgproc.getAffineTransform(targetPoint, referencePoint);
                //final Mat H = Calib3d.findHomography(targetPoint, referencePoint, Calib3d.RANSAC, 5);
                final Mat H = Imgproc.getPerspectiveTransform(targetPoint, referencePoint);
                final Mat warpedMat = new Mat();
                Imgproc.warpPerspective(imagePoints.getMatImage(), warpedMat, H, targetImage.getMatImage().size());
                final Optional<ImagePlus> finalImage = this.convertToImage(imagePoints.getFile(), warpedMat);
                return finalImage.map(imagePlus -> new AlignedImage(warpedMat, H, imagePlus));
            }else{
                throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                        "In order to use the Affine alignment you must use at least: " + PerspectiveAlignment.LOWER_BOUND + " points.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }


    public Mat getTransformationMatrix(Point[] dstArray, Point[] srcArray) {
        return null;
    }
}
