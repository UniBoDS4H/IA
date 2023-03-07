package com.ds4h.model.alignment.automatic.pointDetector.surfDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.List;

public class SURFDetector extends PointDetector {

    private final SURF detector = SURF.create();
    private final BFMatcher matcher = BFMatcher.create();
    public SURFDetector(){
        super();
    }

    @Override
    public void detectPoint(final Mat targetImage, final ImagePoints imagePoint) {
        this.keypoints1List.clear();
        this.keypoints2List.clear();
        this.matchesList.clear();
        final Mat imagePointMat = super.toGrayscale(Imgcodecs.imread(imagePoint.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
        final Mat targetImageMat = super.toGrayscale(targetImage);

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
            if (match.distance < maxDist * minDist) {
                goodMatches.add(match);
            }
        }
        this.matchesList.addAll(goodMatches);
        // convert the matrices of keypoints in to list of keypoints, which represent the list of keypoints in the two images
        this.keypoints1List.addAll(keypoints1.toList());
        this.keypoints2List.addAll(keypoints2.toList());
    }

    @Override
    public void matchPoint(final ImagePoints targetImage, final ImagePoints imagePoints) {
        this.points1.clear();
        this.points2.clear();
        this.matchesList.forEach(match -> {
                    this.points1.add(this.keypoints1List.get(match.queryIdx).pt);
                    this.points2.add(this.keypoints2List.get(match.trainIdx).pt);
                });


        this.points1.forEach(imagePoints::addPoint);
        this.points2.forEach(targetImage::addPoint);
        System.out.println("INSIDE MATCH SURF DETECTOR :" + imagePoints.getMatOfPoint());
    }
}
