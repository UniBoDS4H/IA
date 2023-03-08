package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;

import java.util.Optional;

public interface AlignmentAlgorithm {


    Optional<AlignedImage> align(ImagePoints targetImage, ImagePoints imageToShift) throws IllegalArgumentException;

    Mat getTransformationMatrix(MatOfPoint2f srcPoints, MatOfPoint2f dstPoints);

    void transform(Mat source, Mat destination, Mat H, int nPoints);
}
