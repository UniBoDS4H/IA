package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

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
     * @param source : the source image used as reference
     * @param  target : the target to align
     * @throws IllegalArgumentException : in case the number of corners is not correct
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected Optional<AlignedImage> align(final ImageCorners source, final ImageCorners target) throws IllegalArgumentException{
        try {
            if(source.numberOfCorners() >= LOWER_BOUND && target.numberOfCorners() >= LOWER_BOUND) {
                final MatOfPoint2f referencePoint = source.getMatOfPoint();
                final MatOfPoint2f targetPoint = target.getMatOfPoint();
                final Mat H = Calib3d.findHomography(targetPoint, referencePoint, Calib3d.RANSAC, 5);
                final Mat warpedMat = new Mat();
                Imgproc.warpPerspective(target.getMatImage(), warpedMat, H, source.getMatImage().size());
                final Optional<ImagePlus> finalImage = this.convertToImage(target.getFile(), warpedMat);
                return finalImage.map(imagePlus -> new AlignedImage(warpedMat, H, imagePlus));
            }else{
                throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                        "In order to use the RANSAC alignment you must use at least: " + RansacAlignment.LOWER_BOUND + " points.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public int neededPoints(){
        return RansacAlignment.LOWER_BOUND;
    }
}
