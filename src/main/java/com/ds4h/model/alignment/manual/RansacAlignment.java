package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.ManualAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

/**
 * This class is used for the manual alignment using the Homography technique
 */
public class RansacAlignment extends ManualAlgorithm {

    public static final int LOWER_BOUND = 4;

    public RansacAlignment(){
        super();
    }
    /**
     * Manual alignment using the Homography alignment
     *
     * @param targetImage : the source image used as reference
     * @param imagePoints : the target to align
     * @param targetSize
     * @return : the list of all the images aligned to the source
     * @throws IllegalArgumentException : in case the number of corners is not correct
     */
    @Override
    public Optional<AlignedImage> align(final MatOfPoint2f targetImage, final ImagePoints imagePoints, Size targetSize) throws IllegalArgumentException{
        try {
            if(targetImage.toList().size() >= LOWER_BOUND && imagePoints.numberOfPoints() >= LOWER_BOUND) {
                final Mat homography = null;// this.getTransformationMatrix(imagePoints, targetImage);//Calib3d.findHomography(referencePoint, targetPoint, Calib3d.RANSAC, 5);
                final Mat translationMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
                translationMatrix.put(0, 2, homography.get(0, 2)[0]);
                translationMatrix.put(1, 2, homography.get(1, 2)[0]);

                final Mat warpedMat = new Mat();
                Imgproc.warpPerspective(imagePoints.getMatImage(), warpedMat, translationMatrix, targetSize);
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

    @Override
    public Mat getTransformationMatrix(final ImagePoints imageToAlign, final ImagePoints targetImage) {
        final MatOfPoint2f targetPoint = new MatOfPoint2f();
        targetPoint.fromArray(targetImage.getPoints());
        final MatOfPoint2f referencePoint = new MatOfPoint2f();
        referencePoint.fromArray(imageToAlign.getPoints());
        return Calib3d.findHomography(referencePoint, targetPoint, Calib3d.RANSAC, 5);
    }

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H){
        Core.perspectiveTransform(source, destination, H);
    }
}
