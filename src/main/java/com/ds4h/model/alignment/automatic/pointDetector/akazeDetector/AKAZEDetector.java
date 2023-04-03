package com.ds4h.model.alignment.automatic.pointDetector.akazeDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.AKAZE;
import org.opencv.features2d.BFMatcher;

import java.util.List;

public class AKAZEDetector extends PointDetector {

    private final AKAZE detector = AKAZE.create();
    private final BFMatcher matcher = BFMatcher.create(BFMatcher.BRUTEFORCE_HAMMING);

    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor) {

        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        final Mat grayImg = scalingFactor > 1 ?  this.createPyramid(imagePoint.getGrayScaleMat(), scalingFactor) :
                imagePoint.getGrayScaleMat();

        final Mat grayTarget = super.getMatCache().isSet() ?
                super.getMatCache().getTargetMatrix() :
                super.getMatCache().setTargetMatrix(scalingFactor > 1 ?
                        this.createPyramid(targetImage.getGrayScaleMat(), scalingFactor) :
                        targetImage.getGrayScaleMat());
        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();
        detector.detectAndCompute(grayImg, new Mat(), keypoints1, descriptors1);
        detector.detectAndCompute(grayTarget, new Mat(), keypoints2, descriptors2);
        grayImg.release();
        final MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);
        descriptors1.release();
        descriptors2.release();
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;
        for (int i = 0; i < matches.rows(); i++) {
            double dist = matches.toList().get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }
        final double threshold = (0.8+this.getFactor()) * min_dist;

        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();
        keypoints1.release();
        keypoints2.release();
        final double scale = Math.pow(2, scalingFactor-1);

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
    }
}
