package com.ds4h.model.alignment.automatic;

import ij.ImagePlus;
import org.bytedeco.opencv.opencv_core.DMatchVector;
import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.bytedeco.opencv.opencv_core.Point2fVector;
import org.bytedeco.opencv.opencv_features2d.BFMatcher;
import org.bytedeco.opencv.opencv_xfeatures2d.SURF;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

import static org.bytedeco.opencv.global.opencv_calib3d.findHomography;
import static org.bytedeco.opencv.global.opencv_core.NORM_L2;
import static org.opencv.calib3d.Calib3d.RANSAC;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.*;

public class SurfAlignment {
    public static Mat align(Mat img1, Mat img2){

        Mat img1Gray = new Mat();
        Mat img2Gray = new Mat();
        cvtColor(img1, img1Gray, COLOR_RGB2GRAY);
        cvtColor(img2, img2Gray, COLOR_RGB2GRAY);

        // Detect keypoints and compute descriptors using the SURF algorithm
        SURF detector = SURF.create();
        KeyPointVector keypoints1 = new KeyPointVector();
        KeyPointVector keypoints2 = new KeyPointVector();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        detector.detectAndCompute(img1Gray, new Mat(), keypoints1, descriptors1);
        detector.detectAndCompute(img2Gray, new Mat(), keypoints2, descriptors2);

        // Match the descriptors using the Brute-Force matcher
        BFMatcher matcher = BFMatcher.create(NORM_L2);
        DMatchVector matches = new DMatchVector();
        matcher.match(descriptors1, descriptors2, matches);

        // Find the best matches
        float max_dist = 0;
        float min_dist = 100;
        for (int i = 0; i < descriptors1.rows(); i++) {
            float dist = matches.get(i).distance();
            if (dist < min_dist)
                min_dist = dist;
            if (dist > max_dist)
                max_dist = dist;
        }
        DMatchVector good_matches = new DMatchVector();
        for (int i = 0; i < descriptors1.rows(); i++) {
            if (matches.get(i).distance() <= Math.max(2 * min_dist, 0.02))
                good_matches.push_back(matches.get(i));
        }
        Point2fVector srcPoints = new Point2fVector();
        Point2fVector dstPoints = new Point2fVector();
        for (int i = 0; i < good_matches.size(); i++) {
            DMatch match = good_matches.get(i);
            KeyPoint kp1 = keypoints1.get(match.queryIdx());
            KeyPoint kp2 = keypoints2.get(match.trainIdx());
            srcPoints.push_back(kp1.pt());
            dstPoints.push_back(kp2.pt());
        }

        // Compute the homography matrix using RANSAC
        Mat mask = new Mat();
        Mat H = findHomography(srcPoints, dstPoints, RANSAC, 3, mask);

        // Warp the second image using the homography matrix
        Mat img2Warped = new Mat();
        warpPerspective(img2, img2Warped, H, img1.size());

        return img2Warped;


         return null;
    }
}
