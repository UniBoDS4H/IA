package com.ds4h.model.alignment.automatic.pointDetector.orbDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.ORB;

import java.util.List;

public class ORBDetector extends PointDetector {

    private final ORB detector = ORB.create();
    private final BFMatcher matcher = BFMatcher.create();

    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint) {

        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();


        Mat grayImg = super.getScalingFactor() > 1 ?  this.createPyramid(imagePoint.getGrayScaleMat(), super.getScalingFactor()) :
                imagePoint.getGrayScaleMat();

        Mat grayTarget = super.getMatCache().isAlreadyDetected() ?
                null : super.getScalingFactor() > 1 ?
                this.createPyramid(targetImage.getGrayScaleMat(), super.getScalingFactor()) :
                targetImage.getGrayScaleMat();

        // Start detection
        this.detector.detectAndCompute(grayImg, new Mat(),  keypoints1, descriptors1);
        IJ.log("[SIFT DETECTOR] Detected points for the first image.");
        this.detector.detectAndCompute(grayTarget, new Mat(), keypoints2, descriptors2);
        grayImg.release();
        System.gc();
        // End detection

        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2
        //matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        descriptors1.release();
        descriptors2.release();
        System.gc();

        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;
        for (int i = 0; i < matches.rows(); i++) {
            double dist = matches.toList().get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        final double threshold = (2.0 + this.getFactor()) * min_dist;
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();
        keypoints1.release();
        keypoints2.release();

        final double scale = Math.pow(2, super.getScalingFactor()-1);
        matches.toList().stream()
                .filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {
                    final Point queryScaled = new Point(
                            keypoints1List.get(goodMatch.queryIdx).pt.x * (scale),
                            keypoints1List.get(goodMatch.queryIdx).pt.y * (scale));
                    final Point trainScaled = new Point(
                            keypoints2List.get(goodMatch.trainIdx).pt.x * (scale),
                            keypoints2List.get(goodMatch.trainIdx).pt.y * (scale));
                    imagePoint.addPoint(queryScaled);
                    targetImage.addPoint(trainScaled);
                });
        matches.release();
    }
}
