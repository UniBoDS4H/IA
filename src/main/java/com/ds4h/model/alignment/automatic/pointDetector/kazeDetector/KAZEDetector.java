package com.ds4h.model.alignment.automatic.pointDetector.kazeDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.*;

import java.util.List;
import java.util.Objects;

public class KAZEDetector extends PointDetector {
    private final KAZE detector = KAZE.create();
    private final BFMatcher matcher = BFMatcher.create();
    final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();

    final Mat descriptors1 = new Mat();
    public KAZEDetector(){
        super();

    }
    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint) {

        Mat grayImg = super.getScalingFactor() > 1 ?  this.createPyramid(imagePoint.getGrayScaleMat(), super.getScalingFactor()) :
                imagePoint.getGrayScaleMat();

        Mat grayTarget = super.getMatCache().isAlreadyDetected() ?
                null : super.getScalingFactor() > 1 ?
                this.createPyramid(targetImage.getGrayScaleMat(), super.getScalingFactor()) :
                targetImage.getGrayScaleMat();

        grayImg = imagePoint.toImprove() ? super.improveMatrix(grayImg) : grayImg;
        grayTarget = Objects.nonNull(grayTarget) && targetImage.toImprove() ? super.improveMatrix(grayTarget) : grayTarget;


        this.detector.detectAndCompute(grayImg, new Mat(), this.keypoints1, this.descriptors1);
        grayImg.release();
        if(!super.getMatCache().isAlreadyDetected()) {
            final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
            final Mat descriptors2 = new Mat();
            this.detector.detectAndCompute(grayTarget, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints
            super.getMatCache().setDetection(descriptors2, keypoints2);
            grayTarget.release();
        }
        System.gc();
        // Match keypoints between the two images using a BFMatcher
        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(this.descriptors1, super.getMatCache().getDescriptor(), matches);

        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;

        this.descriptors1.release();
        for(final DMatch match : matches.toList()){
            final double dist = match.distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }


        final double threshold = (1.4)+this.getFactor();
        final List<KeyPoint> keypoints1List = this.keypoints1.toList();//originalKeyPoints.toList();
        final List<KeyPoint> keypoints2List = super.getMatCache().getKeyPoints();//targetKeyPoints.toList();
        this.keypoints1.release();

        final double scale = super.getScalingFactor() > 1 ?  Math.pow(2, super.getScalingFactor()-1) : 1;

        matches.toList().stream().filter(match -> match.distance < threshold)
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
    }

}
