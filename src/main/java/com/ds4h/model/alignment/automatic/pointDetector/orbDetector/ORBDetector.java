package com.ds4h.model.alignment.automatic.pointDetector.orbDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.ORB;

import java.util.List;

public class ORBDetector extends PointDetector {

    private final ORB detector = ORB.create();
    private final BFMatcher matcher = BFMatcher.create();

    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor) {

        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        this.detector.detectAndCompute(imagePoint.getGrayScaleMat(), new Mat(), keypoints1, descriptors1); // Detect and save the keypoints

        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        this.detector.detectAndCompute(targetImage.getGrayScaleMat(), new Mat(), keypoints2, descriptors2); // Detect and save the keypoints

        // Detect key points for the second image

        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final MatOfDMatch matches = new MatOfDMatch();
        //final MatOfDMatch matches_ = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2
        //matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();
        /*
        matches.toList().stream()
                .peek(match -> System.out.println(match.distance))
                .filter(match -> match.distance < 400)
                .forEach(goodMatch -> {
                    imagePoint.addPoint(keypoints1List.get(goodMatch.queryIdx).pt);
                    targetImage.addPoint(keypoints2List.get(goodMatch.trainIdx).pt);
                });
         */
        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;
        for (int i = 0; i < matches.rows(); i++) {
            double dist = matches.toList().get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        final double threshold = (2.0 + this.getFactor()) * min_dist;

        matches.toList().stream()
                .filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {
                    imagePoint.addPoint(keypoints1List.get(goodMatch.queryIdx).pt);
                    targetImage.addPoint(keypoints2List.get(goodMatch.trainIdx).pt);
                });
    }
}
