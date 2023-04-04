package com.ds4h.model.alignment.automatic.pointDetector.kazeDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import org.opencv.core.*;
import org.opencv.features2d.*;

import java.util.List;

public class KAZEDetector extends PointDetector {
    private final KAZE detector = KAZE.create();
    private final BFMatcher matcher = BFMatcher.create();
    final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
    final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

    final Mat descriptors1 = new Mat();
    final Mat descriptors2 = new Mat();
    public KAZEDetector(){
        super();

    }
    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor) {




        final Mat grayImg = scalingFactor > 1 ?  this.createPyramid(imagePoint.getGrayScaleMat(), scalingFactor) :
                imagePoint.getGrayScaleMat();


        final Mat grayTarget = super.getMatCache().isAlreadyDetected() ?
                null : scalingFactor > 1 ?
                this.createPyramid(targetImage.getGrayScaleMat(), scalingFactor) :
                targetImage.getGrayScaleMat();


        this.detector.detectAndCompute(grayImg, new Mat(), this.keypoints1, this.descriptors1);
        this.detector.detectAndCompute(grayTarget, new Mat(), this.keypoints2, this.descriptors2);
        grayImg.release();
        // Match keypoints between the two images using a BFMatcher
        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(this.descriptors1, this.descriptors2, matches);

        double max_dist = 0;
        double min_dist = Double.MAX_VALUE;

        this.descriptors1.release();
        this.descriptors2.release();
        for(final DMatch match : matches.toList()){
            final double dist = match.distance;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }


        final double threshold = (1.4)+this.getFactor();
        final List<KeyPoint> keypoints1List = this.keypoints1.toList();//originalKeyPoints.toList();
        final List<KeyPoint> keypoints2List = this.keypoints2.toList();//targetKeyPoints.toList();
        this.keypoints1.release();
        this.keypoints2.release();
        final double scale = scalingFactor > 1 ?  Math.pow(2, scalingFactor-1) : 1;

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
