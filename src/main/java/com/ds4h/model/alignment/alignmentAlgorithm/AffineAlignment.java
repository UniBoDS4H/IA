package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

public class AffineAlignment implements AlignmentAlgorithm{
    public static int LOWER_BOUND = 3;
    @Override
    public Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException {
        if(targetImage.numberOfPoints() >= LOWER_BOUND && imageToShift.numberOfPoints() >= LOWER_BOUND) {
            final Mat imageToShiftMat = imageToShift.getMatImage();
                final Mat alignedImage = new Mat();
                final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
                Imgproc.warpAffine(imageToShiftMat, alignedImage, transformationMatrix, targetImage.getMatImage().size());
                final Optional<ImagePlus> finalImage = Optional.of(ImagingConversion.matToImagePlus(alignedImage, imageToShift.getName(), ip));
                return finalImage.map(imagePlus -> new AlignedImage(transformationMatrix, imagePlus));
        }else {
            throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                    "In order to use the Affine alignment you must use: " + AffineAlignment.LOWER_BOUND + " points.");
        }
    }

    @Override
    public Mat getTransformationMatrix(MatOfPoint2f srcPoints, MatOfPoint2f dstPoints) {
        return Calib3d.estimateAffine2D(srcPoints, dstPoints);
    }

    @Override
    public void transform(Mat source, Mat destination, Mat H, int nPoints) {
        Core.transform(source,destination,H);
    }

    @Override
    public int getLowerBound() {
        return LOWER_BOUND;
    }
}
