package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.process.ImageProcessor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;

public interface AlignmentAlgorithm {

    /**
     *
     * @param targetImage a
     * @param imageToShift b
     * @param ip c
     * @return d
     * @throws IllegalArgumentException e
     */
    AlignedImage align(final ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException;

    /**
     *
     * @param srcPoints a
     * @param dstPoints b
     * @return c
     */
    Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints);

    /**
     *
     * @param source a
     * @param destination b
     * @param H c
     */
    void transform(final Mat source, final Mat destination, Mat H);

    /**
     *
     * @return a
     */
    int getLowerBound();

    /**
     *
     * @param overload a
     */
    public void setPointOverload(PointOverloadEnum overload);

    /**
     *
     * @return a
     */
    PointOverloadEnum getPointOverload();
}
