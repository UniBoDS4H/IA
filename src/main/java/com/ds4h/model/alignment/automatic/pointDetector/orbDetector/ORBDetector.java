package com.ds4h.model.alignment.automatic.pointDetector.orbDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;

import java.util.ArrayList;
import java.util.List;

public class ORBDetector extends PointDetector {
    private final ORB orb = ORB.create();
    public ORBDetector(){

    }
    @Override
    public void detectPoint(Mat targetImage, ImagePoints imagePoint) {
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        orb.detectAndCompute(targetImage, new Mat(), keypoints1, descriptors1);
        orb.detectAndCompute(imagePoint.getMatImage(), new Mat(), keypoints2, descriptors2);

        // Match the keypoints from both images
        MatOfDMatch matches = new MatOfDMatch();
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        matcher.match(descriptors1, descriptors2, matches);

        // Filter the matches to keep only the good ones
        List<DMatch> matchesList = matches.toList();
        List<DMatch> goodMatchesList = new ArrayList<>();
        for (DMatch match : matchesList) {
            if (match.distance < 30) { // Adjust this threshold to your needs
                goodMatchesList.add(match);
            }
        }
        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(goodMatchesList);
    }

    @Override
    public void matchPoint(ImagePoints targetImage, ImagePoints imagePoints) {

    }
}
