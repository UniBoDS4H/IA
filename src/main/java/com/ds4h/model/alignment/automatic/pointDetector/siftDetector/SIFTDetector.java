package com.ds4h.model.alignment.automatic.pointDetector.siftDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.SIFT;

import java.util.List;

public class SIFTDetector extends PointDetector {
    private final SIFT sift = SIFT.create();
    private final DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);


    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor) {
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

        final Mat grayImg = this.createPyramid(imagePoint.getGrayScaleMat(), scalingFactor);
        final Mat grayTarget = this.createPyramid(targetImage.getGrayScaleMat(), scalingFactor);

        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();
        //
        this.sift.detect(grayImg, keypoints1);
        this.sift.compute(grayImg, keypoints1, descriptors1);
        IJ.log("[SIFT DETECTOR] Detected points for the first image.");
        grayImg.release();
        System.gc();
        this.sift.detect(grayTarget, keypoints2);
        this.sift.compute(grayTarget, keypoints2, descriptors2);
        grayTarget.release();
        IJ.log("[SIFT DETECTOR] Detected points for the target image.");
        System.gc();
        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2
        descriptors1.release();
        descriptors2.release();
        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;

        for(final DMatch match : matches.toList()){
            final double dist = match.distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }

        final double threshold = (1.8 + this.getFactor()) * min_dist;
        final List<KeyPoint> keypoints1List = keypoints1.toList();//originalKeyPoints.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();//targetKeyPoints.toList();
        keypoints1.release();
        keypoints2.release();
        final double scale = Math.pow(2, scalingFactor-1);
        matches.toList().stream()
                .filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {
                    final Point queryScaled = new Point(
                            keypoints1List.get(goodMatch.queryIdx).pt.x * (scale),
                            keypoints1List.get(goodMatch.queryIdx).pt.y * (scale));
                    final Point trainScaled = new Point(
                            keypoints2List.get(goodMatch.trainIdx).pt.x * (scale),
                            keypoints2List.get(goodMatch.trainIdx).pt.y * (scale));
                    imagePoint.addPoint(queryScaled);
                    targetImage.addPoint(trainScaled);
                });
        IJ.log("[SIFT DETECTOR] End Detection.");
        matches.release();
    }
}
