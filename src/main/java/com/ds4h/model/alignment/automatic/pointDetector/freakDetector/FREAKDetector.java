package com.ds4h.model.alignment.automatic.pointDetector.freakDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.image.AnalyzableImage;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.SIFT;
import org.opencv.xfeatures2d.FREAK;

import java.util.List;

public class FREAKDetector extends PointDetector {
    private final SIFT detector = SIFT.create();
    private final FREAK extractor = FREAK.create();
    private final DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

    @Override
    public void detectPoint(AnalyzableImage targetImage, AnalyzableImage imagePoint) {
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();

        Mat grayImg = super.getScalingFactor() > 1 ?  this.createPyramid(imagePoint.getGrayScaleMat(), super.getScalingFactor()) :
                imagePoint.getGrayScaleMat();
        grayImg = imagePoint.toImprove() ? super.improveMatrix(grayImg) : grayImg;

        this.detector.detect(grayImg, keypoints1);
        this.extractor.compute(grayImg, keypoints1, descriptors1);
        grayImg.release();

        if(!super.getMatCache().isAlreadyDetected()) {
            Mat grayTarget = super.getScalingFactor() > 1 ?
                    this.createPyramid(targetImage.getGrayScaleMat(), super.getScalingFactor()) :
                    targetImage.getGrayScaleMat();
            grayTarget = targetImage.toImprove() ? super.improveMatrix(grayTarget) : grayTarget;
            this.detector.detect(grayTarget, keypoints2); // Detect and save the keypoints
            this.extractor.compute(grayTarget, keypoints2, descriptors2);
            super.getMatCache().setDetection(descriptors2, keypoints2);
            grayTarget.release();
            keypoints2.release();
        }

        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(descriptors1, super.getMatCache().getDescriptor(), matches);
        descriptors1.release();

        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;

        for (int i = 0; i < matches.rows(); i++) {
            double dist = matches.toList().get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        final double threshold = (1.1+this.getFactor()) * min_dist;
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = super.getMatCache().getKeyPoints();
        keypoints1.release();
        final double scale = Math.pow(2, super.getScalingFactor()-1);

        this.addKeyPoints(imagePoint, targetImage, keypoints1List, keypoints2List, matches.toList());
    }
}
