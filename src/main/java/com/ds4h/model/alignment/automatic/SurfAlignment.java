package com.ds4h.model.alignment.automatic;

import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.List;

public class SurfAlignment {
    public static Mat align(){
        //Read the two images you want to align using the Imgcodecs class:
        final Mat image1 = Imgcodecs.imread("image1.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        final Mat image2 = Imgcodecs.imread("image2.jpg", Imgcodecs.IMREAD_GRAYSCALE);


        // Detect keypoints and compute descriptors using the SURF algorithm
        final SURF detector = SURF.create();

        // Detect the keypoints and compute the descriptors for both images:
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        detector.detectAndCompute(image1 , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints

        // Detect key points for the second image
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        detector.detectAndCompute(image2, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints


        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final BFMatcher matcher = BFMatcher.create();
        final MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2

        final MatOfDMatch matches_ = new MatOfDMatch();
        matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        final List<DMatch> matchesList = matches.toList();

        // convert the matrices of keypoints in to list of keypoints, which represent the list of keypoints in the two images
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();


        final List<Point> points1 = new ArrayList<>();
        /* the loop is used to iterate through the matches list, and for each match , it adds the corresponding point from the
           first image to the "points1", and the corresponding point from the second image to the "points2" list.
        * */
        for(int i = 0;i<matchesList.size(); i++){
            points1.add(keypoints1List.get(matchesList.get(i).queryIdx).pt);
        }

        final List<Point> points2 = new ArrayList<>();
        for(int i = 0;i<matchesList.size(); i++){
            points2.add(keypoints2List.get(matchesList.get(i).trainIdx).pt);
        }

        final MatOfPoint2f points1_ = new MatOfPoint2f();
        points1_.fromList(points1);
        final MatOfPoint2f points2_ = new MatOfPoint2f();
        points2_.fromList(points2);

        Mat H = Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, 5);

        Mat alignedImage1 = new Mat();
        Imgproc.warpPerspective(image1, alignedImage1, H, image2.size());

        return alignedImage1;
    }
}
