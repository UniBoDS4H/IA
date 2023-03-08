package com.ds4h.model.alignment.automatic.pointDetector.orbDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;

import java.util.ArrayList;
import java.util.List;

public class ORBDetector extends PointDetector {
    private final ORB detector = ORB.create();
    private final BFMatcher matcher = BFMatcher.create(BFMatcher.BRUTEFORCE_HAMMING);
    public ORBDetector(){
        super();
    }
    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint) {
        final Mat imagePointMat = imagePoint.getGrayScaleMat();
        final Mat targetImageMat = targetImage.getGrayScaleMat();

        // Detect the keypoints and compute the descriptors for both images:
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        this.detector.detectAndCompute(imagePointMat , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints


        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        this.detector.detectAndCompute(targetImageMat, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints


        // Detect key points for the second image


        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final MatOfDMatch matches = new MatOfDMatch();
        final MatOfDMatch matches_ = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2
        matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        double maxDist = 0.7;
        double minDist = 0.2;
        final List<DMatch> goodMatches = new ArrayList<>();
        for (final DMatch match : matches.toList()) {
            if (match.distance < 30) {
                goodMatches.add(match);
            }
        }
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();
        goodMatches.forEach(match -> {
            imagePoint.addPoint(keypoints1List.get(match.queryIdx).pt);
            targetImage.addPoint(keypoints2List.get(match.trainIdx).pt);
        });
    }

}
