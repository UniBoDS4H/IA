package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;

import java.util.Optional;

public class AffineAlignment implements AlignmentAlgorithm{
    public static int REQUIRED_POINTS = 3;
    @Override
    public Optional<AlignedImage> align(ImagePoints targetImage, ImagePoints imageToShift) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Mat getTransformationMatrix(MatOfPoint2f srcPoints, MatOfPoint2f dstPoints) {
        return null;
    }

    @Override
    public void transform(Mat source, Mat destination, Mat H, int nPoints) {

    }
}
