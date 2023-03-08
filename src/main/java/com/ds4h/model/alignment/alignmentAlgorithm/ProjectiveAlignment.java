package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

public class ProjectiveAlignment implements AlignmentAlgorithm{
    public static int LOWER_BOUND = 4;
    @Override
    public Optional<AlignedImage> align(ImagePoints targetImage, final ImagePoints imageToShift) throws IllegalArgumentException {
        if(targetImage.getPoints().length >= LOWER_BOUND && imageToShift.getPoints().length >= LOWER_BOUND){
            final Mat imageToShiftMat = imageToShift.getMatImage();
            final Mat alignedImage = new Mat();
            final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
            Imgproc.warpPerspective(imageToShiftMat, alignedImage, transformationMatrix, targetImage.getMatImage().size());
            final Optional<ImagePlus> finalImage = ImagingConversion.fromMatToImagePlus(alignedImage, imageToShift.getName());
            return finalImage.map(imagePlus -> new AlignedImage(alignedImage, transformationMatrix, imagePlus));
        }else{
            throw new IllegalArgumentException("For the Projective alignment the points must be at least: 4");
        }
    }

    @Override
    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints) {
        return Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
    }

    @Override
    public void transform(Mat source, Mat destination, Mat H, int nPoints) {
        Core.perspectiveTransform(source,destination,H);
    }
}
