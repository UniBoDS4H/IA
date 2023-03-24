package com.ds4h.model.alignment.automatic.pointDetector.akazeDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.AKAZE;
import org.opencv.features2d.BFMatcher;

public class AKAZEDetector extends PointDetector {

    private final AKAZE detector = AKAZE.create();
    private final BFMatcher matcher = BFMatcher.create(BFMatcher.BRUTEFORCE_HAMMING);

    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor) {

        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();
        detector.detectAndCompute(imagePoint.getGrayScaleMat(), new Mat(), keypoints1, descriptors1);
        detector.detectAndCompute(targetImage.getGrayScaleMat(), new Mat(), keypoints2, descriptors2);

        final MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);
        final double threshold = 0.2 * 0.7;
        matches.toList().stream()
                .filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {
                    imagePoint.addPoint(keypoints1.toList().get(goodMatch.queryIdx).pt);
                    targetImage.addPoint(keypoints2.toList().get(goodMatch.trainIdx).pt);
                });
    }
}
