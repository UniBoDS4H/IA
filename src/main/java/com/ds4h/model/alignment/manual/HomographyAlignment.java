package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.Pair;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
    protected   Optional<Pair<ImagePlus, Mat>> align(final ImageCorners source, final ImageCorners target){
        try {
            final MatOfPoint2f referencePoint = source.getMatOfPoint();
            final MatOfPoint2f targetPoint = target.getMatOfPoint();
            //final Mat H = Imgproc.getAffineTransform(targetPoint, referencePoint);
            final Mat H = Calib3d.estimateAffine2D(targetPoint, referencePoint);

            final Mat warpedMat = new Mat();
            Imgproc.warpAffine(target.getMatImage(), warpedMat, H, source.getMatImage().size(),Imgproc.INTER_LINEAR, 0, new Scalar(0, 0, 0));
            final Optional<ImagePlus> finalImage = this.convertToImage(target.getFile(), warpedMat);
            return finalImage.map(image -> new Pair<>(image, warpedMat));
        }catch (Exception ex){
            IJ.showMessage(ex.getMessage());
        }
        return Optional.empty();

    }
}
