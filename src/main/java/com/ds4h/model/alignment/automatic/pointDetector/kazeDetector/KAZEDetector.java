package com.ds4h.model.alignment.automatic.pointDetector.kazeDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.*;

public class KAZEDetector extends PointDetector {
    private final KAZE detector = KAZE.create();
    private final BFMatcher matcher = BFMatcher.create();
    public KAZEDetector(){
        super();

    }
    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor) {


        // Detect keypoints and compute descriptors for both images
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();
        detector.detectAndCompute(imagePoint.getGrayScaleMat(), new Mat(), keypoints1, descriptors1);
        detector.detectAndCompute(targetImage.getGrayScaleMat(), new Mat(), keypoints2, descriptors2);

        // Match keypoints between the two images using a BFMatcher
        final MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);
        final double threshold = (0.7 * 0.2)+this.getFactor();
        matches.toList().stream().filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {
                    imagePoint.addPoint(keypoints1.toList().get(goodMatch.queryIdx).pt);
                    targetImage.addPoint(keypoints2.toList().get(goodMatch.trainIdx).pt);
                });
    }

}
