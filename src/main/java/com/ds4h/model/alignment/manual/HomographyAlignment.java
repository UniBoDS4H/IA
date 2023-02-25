package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.Pair;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

public class HomographyAlignment extends AlignmentAlgorithm {


    public HomographyAlignment(){
        super();
    }

    /**
     * Manual alignment using the Homography alignment
     * @param source : the source image used as reference
     * @param  target : the target to align
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected   Optional<AlignedImage> align(final ImageCorners source, final ImageCorners target){
        try {
            final MatOfPoint2f referencePoint = source.getMatOfPoint();
            final MatOfPoint2f targetPoint = target.getMatOfPoint();
            final Mat H = Imgproc.getAffineTransform(targetPoint, referencePoint);
            //final Mat H = Calib3d.findHomography(targetPoint, referencePoint);
            final Mat warpedMat = new Mat();
            Imgproc.warpAffine(target.getMatImage(), warpedMat, H, source.getMatImage().size(), Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP);            final Optional<ImagePlus> finalImage = this.convertToImage(target.getFile(), warpedMat);
            return finalImage.map(imagePlus -> new AlignedImage(warpedMat, H, imagePlus));
        }catch (Exception ex){
            IJ.showMessage(ex.getMessage());
        }
        return Optional.empty();

    }
}
