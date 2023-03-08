package com.ds4h.model.alignment.automatic.pointDetector.orbDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.xfeatures2d.SIFT;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;

public class SIFTDetector extends PointDetector {
    private final AKAZE detector = AKAZE.create();
    private final BFMatcher matcher = BFMatcher.create();
    public SIFTDetector(){
        super();

    }
    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint) {
        final Mat grayImgPoint = imagePoint.getGrayScaleMat();
        final Mat grayTarget = imagePoint.getGrayScaleMat();

        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        this.detector.detectAndCompute(grayImgPoint , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints


        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        this.detector.detectAndCompute(grayTarget, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints

        // Detect key points for the second image

        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final MatOfDMatch matches = new MatOfDMatch();
        final MatOfDMatch matches_ = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2
        matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        final double threshold = 0.2*0.7;
        final List<DMatch> goodMatches = new ArrayList<>();
        for (final DMatch match : matches.toList()) {
            if (match.distance < threshold) {
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
