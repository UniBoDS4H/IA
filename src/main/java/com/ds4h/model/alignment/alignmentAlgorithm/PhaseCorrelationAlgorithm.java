package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

public class PhaseCorrelationAlgorithm implements AlignmentAlgorithm{
    @Override
    public Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imageToShift) throws IllegalArgumentException {
        // Compute the average image

        final Mat targetMat = targetImage.getMatImage();
        final Mat imageShift = imageToShift.getMatImage();
        Mat sum = new Mat();
        Core.add(targetMat, imageShift, sum);
        Core.add(sum, imageShift, sum);
        Mat avgImg = new Mat();
        Core.divide(sum, new Scalar(3), avgImg);
        Mat shift = new Mat();
        Core.phase(avgImg, imageShift, shift);
        final Mat alignedImg = new Mat();
        Mat transMat = new Mat(2, 3, CvType.CV_32F);
        transMat.put(0, 0, 1, 0, shift.get(0, 0)[0], 0, 1, shift.get(1, 0)[0]);
        Imgproc.warpAffine(imageShift, alignedImg, transMat, imageShift.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(0, 0, 0));
        return null;
    }

    @Override
    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints) {
        return null;
    }

    @Override
    public void transform(final Mat source, final Mat destination, Mat H, int nPoints) {

    }

    @Override
    public int getLowerBound() {
        return 0;
    }
}
