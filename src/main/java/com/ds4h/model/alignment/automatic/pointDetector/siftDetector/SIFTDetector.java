package com.ds4h.model.alignment.automatic.pointDetector.siftDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.SIFT;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class SIFTDetector extends PointDetector {
    private final SIFT sift = SIFT.create();
    private final DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
    private static final double THRESHOLD = 300;
    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint) {
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        final Mat grayImg = new Mat();
        final Mat grayTarget = new Mat();
        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();
        //sift.detectAndCompute(imagePoint.getGrayScaleMat(), new Mat(), keypoints1, descriptors1);
        Imgproc.resize(imagePoint.getGrayScaleMat(), grayImg,
                new Size(imagePoint.getWidth()*0.25,
                imagePoint.getHeight()*0.25));
        IJ.log("[SIFT DETECTOR] New gray img matrix: " + grayImg);
        Imgproc.resize(targetImage.getGrayScaleMat(), grayTarget,
                new Size(imagePoint.getWidth()*0.25,
                        imagePoint.getHeight()*0.25));
        sift.detectAndCompute(grayImg, new Mat(), keypoints2, descriptors2);
        IJ.log("[SIFT DETECTOR] Detected points for the first image.");
        if(true){
            return;
        }
        sift.detectAndCompute(grayTarget, new Mat(), keypoints2, descriptors2);

        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2

        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;

        for(final DMatch match : matches.toList()){
            final double dist = match.distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        final double threshold = (1.8 + this.getFactor()) * min_dist;
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
