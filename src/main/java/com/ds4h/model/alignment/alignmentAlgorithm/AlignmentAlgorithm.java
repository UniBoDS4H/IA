package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.process.ImageProcessor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;

public interface AlignmentAlgorithm {

    AlignedImage align(final ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException;

    Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints);

    void transform(final Mat source, final Mat destination, Mat H, final int nPoints);

    int getLowerBound();

}
