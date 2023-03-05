package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class is used for the manual alignment using the Homography technique
 */
public class RansacAlignment extends AlignmentAlgorithm {

    public static final int LOWER_BOUND = 4;

    public RansacAlignment(){
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
                final MatOfPoint2f targetPoint = targetImage.getMatOfPoint();
                final MatOfPoint2f referencePoint = imagePoints.getMatOfPoint();
                System.out.println(targetPoint);
                final Mat homography = Calib3d.findHomography(referencePoint, targetPoint, Calib3d.RANSAC, 5);
                final Mat translationMatrix = Mat.eye(2, 3, CvType.CV_32FC1);
                translationMatrix.put(0, 2, homography.get(0, 2)[0]);
                translationMatrix.put(1, 2, homography.get(1, 2)[0]);

                final Mat warpedMat = new Mat();
                Imgproc.warpAffine(imagePoints.getMatImage(), warpedMat, translationMatrix, targetImage.getMatImage().size());
                final Optional<ImagePlus> finalImage = this.convertToImage(imagePoints.getFile(), warpedMat);
                return finalImage.map(imagePlus -> new AlignedImage(warpedMat, translationMatrix, imagePlus));
            }else{
                throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                        "In order to use the RANSAC alignment you must use at least: " + RansacAlignment.LOWER_BOUND + " points.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public Mat getTransformationMatrix(Point[] dstArray, Point[] srcArray) {
        return null;
    }
}
