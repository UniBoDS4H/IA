package com.ds4h.model.alignment.automatic.pointDetector.briskDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.BRISK;

import java.util.List;

public class BRISKDetector extends PointDetector {

    private final BRISK brisk = BRISK.create();
    private final BFMatcher matcher = BFMatcher.create();
    public BRISKDetector(){
        super();
    }
    @Override
    public void detectPoint(ImagePoints targetImage, ImagePoints imagePoint) {

        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();
        brisk.detectAndCompute(imagePoint.getGrayScaleMat(), new Mat(), keypoints1, descriptors1);
        brisk.detectAndCompute(targetImage.getGrayScaleMat(), new Mat(), keypoints2, descriptors2);

        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2

        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;

        for (int i = 0; i < matches.rows(); i++) {
            double dist = matches.toList().get(i).distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }
        double threshold = 1.8 * min_dist;
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();
        matches.toList().stream().filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {
                    imagePoint.addPoint(keypoints1List.get(goodMatch.queryIdx).pt);
                    targetImage.addPoint(keypoints2List.get(goodMatch.trainIdx).pt);
                });

    }
}
