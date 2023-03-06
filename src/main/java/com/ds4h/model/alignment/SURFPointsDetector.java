package com.ds4h.model.alignment;

import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.List;

public class SURFPointsDetector {
    public void detect(ImagePoints imageToShift, ImagePoints targetImage){
        Mat imageToShiftMat = imageToShift.getMatImage();
        Mat targetImageMat = targetImage.getMatImage();
        final SURF detector = SURF.create();

        // Detect the keypoints and compute the descriptors for both images:
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        detector.detectAndCompute(imageToShiftMat , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints


        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        detector.detectAndCompute(targetImageMat, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints


        // Detect key points for the second image


        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final BFMatcher matcher = BFMatcher.create();
        final MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2

        final MatOfDMatch matches_ = new MatOfDMatch();
        matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        double maxDist = 0.7;
        double minDist = 0.2;
        List<DMatch> goodMatches = new ArrayList<>();
        for (DMatch match : matches.toList()) {
            if (match.distance < maxDist * minDist) {
                goodMatches.add(match);
            }
        }

        Mat imgMatches = new Mat();
        Features2d.drawMatches(imageToShiftMat, keypoints1, targetImageMat, keypoints2,
                new MatOfDMatch(goodMatches.toArray(new DMatch[0])), imgMatches,
                Scalar.all(-1), Scalar.all(-1), new MatOfByte(), Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);

        // Find good matches
        List<Point> points1 = new ArrayList<>();
        List<Point> points2 = new ArrayList<>();
        for (DMatch match : goodMatches) {
            imageToShift.addPoint(keypoints1.toList().get(match.queryIdx).pt);
            targetImage.addPoint(keypoints2.toList().get(match.trainIdx).pt);
        }
    }
}
