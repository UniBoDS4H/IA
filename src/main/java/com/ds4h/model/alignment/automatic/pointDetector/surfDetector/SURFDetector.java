package com.ds4h.model.alignment.automatic.pointDetector.surfDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SURFDetector extends PointDetector {

    private final SURF detector = SURF.create();
    private final BFMatcher matcher = BFMatcher.create();
    public SURFDetector(){
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
        final double threshold = 0.2*0.7;
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();
        matches.toList().stream()
                .filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {
                    imagePoint.addPoint(keypoints1List.get(goodMatch.queryIdx).pt);
                    targetImage.addPoint(keypoints2List.get(goodMatch.trainIdx).pt);
                });
    }

}
