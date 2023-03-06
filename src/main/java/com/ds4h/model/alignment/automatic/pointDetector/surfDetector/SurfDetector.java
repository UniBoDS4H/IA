package com.ds4h.model.alignment.automatic.pointDetector.surfDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.List;

public class SurfDetector extends PointDetector {

    private final SURF detector = SURF.create();
    public SurfDetector(){
        super();
    }

    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint) {
        this.keypoints1List.clear();
        this.keypoints2List.clear();
        this.matchesList.clear();
        final Mat imagePointMat = super.toGrayscale(Imgcodecs.imread(imagePoint.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
        final Mat targetImageMat = super.toGrayscale(Imgcodecs.imread(targetImage.getPath(), Imgcodecs.IMREAD_ANYCOLOR));

        // Detect the keypoints and compute the descriptors for both images:
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        this.detector.detectAndCompute(imagePointMat , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints


        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        this.detector.detectAndCompute(targetImageMat, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints
        // Detect key points for the second image
        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final BFMatcher matcher = BFMatcher.create();
        final MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2

        final MatOfDMatch matches_ = new MatOfDMatch();
        matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        this.matchesList.addAll(matches.toList());
        // convert the matrices of keypoints in to list of keypoints, which represent the list of keypoints in the two images
        super.keypoints1List.addAll(keypoints1.toList());
        super.keypoints2List.addAll(keypoints2.toList());
    }

    @Override
    public void matchPoint() {
        this.points1.clear();
        this.points2.clear();
        this.matchesList.forEach(match -> {
                    this.points1.add(this.keypoints1List.get(match.queryIdx).pt);
                    this.points2.add(this.keypoints2List.get(match.trainIdx).pt);
                });

    }
}
