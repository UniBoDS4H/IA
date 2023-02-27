package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

public class AffineAlignment extends AlignmentAlgorithm {
    private static final int REQUIRED_POINTS = 3;
    public AffineAlignment(){
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
            if(source.numberOfCorners() == REQUIRED_POINTS && target.numberOfCorners() == REQUIRED_POINTS) {
                final MatOfPoint2f referencePoint = source.getMatOfPoint();
                final MatOfPoint2f targetPoint = target.getMatOfPoint();
                final Mat H = Imgproc.getAffineTransform(targetPoint, referencePoint);
                //final Mat H = Calib3d.estimateAffine2D(targetPoint, referencePoint);
                final Mat warpedMat = new Mat();
                Imgproc.warpAffine(target.getMatImage(), warpedMat, H, source.getMatImage().size(), Imgproc.INTER_LINEAR, 0, new Scalar(0, 0, 0));
                final Optional<ImagePlus> finalImage = this.convertToImage(target.getFile(), warpedMat);
                return finalImage.map(imagePlus -> new AlignedImage(warpedMat, H, imagePlus));
            }else{
                throw new IllegalArgumentException("The number of corners inside the source image or inside the target image is not correct.\n" +
                        "In order to use the Affine alignment you must use: " + AffineAlignment.REQUIRED_POINTS + " corners.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public int neededPoints(){
        return AffineAlignment.REQUIRED_POINTS;
    }
}
