package com.ds4h.model.alignment.automatic.pointDetector.siftDetector;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.image.AnalyzableImage;
import ij.IJ;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SIFT;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

public class SIFTDetector extends PointDetector {
    private final SIFT sift = SIFT.create();
    private final DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);

    public SIFTDetector(){
    }

    @Override
    public void detectPoint(@NotNull final AnalyzableImage targetImage, @NotNull final AnalyzableImage imagePoint) {
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final Mat descriptors1 = new Mat();


        Mat grayImg = super.getScalingFactor() > 1 ?  this.createPyramid(imagePoint.getGrayScaleMat(), super.getScalingFactor()) :
                imagePoint.getGrayScaleMat();

        grayImg = imagePoint.toImprove() ? super.improveMatrix(grayImg) : grayImg;


        //
        this.sift.detect(grayImg, keypoints1);
        this.sift.compute(grayImg, keypoints1, descriptors1);

        IJ.log("[SIFT DETECTOR] Detected points for the first image.");
        grayImg.release();
        if(!super.getMatCache().isAlreadyDetected()) {

            Mat grayTarget = super.getScalingFactor() > 1 ?
                    this.createPyramid(targetImage.getGrayScaleMat(), super.getScalingFactor()) :
                    targetImage.getGrayScaleMat();
            grayTarget = targetImage.toImprove() ? super.improveMatrix(grayTarget) : grayTarget;

            final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
            final Mat descriptors2 = new Mat();
            this.sift.detectAndCompute(grayTarget, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints
            super.getMatCache().setDetection(descriptors2, keypoints2);
            grayTarget.release();
        }
        IJ.log("[SIFT DETECTOR] Detected points for the target image.");

        List<MatOfDMatch> knnMatches = new LinkedList<>();
        this.matcher.knnMatch(descriptors1, super.getMatCache().getDescriptor(), knnMatches, 2);

        List<KeyPoint> keypoints1List = keypoints1.toList();
        List<KeyPoint> keypoints2List = super.getMatCache().getKeyPoints();
        final double scale = Math.pow(2, super.getScalingFactor()-1);
        List<DMatch> goodMatches = new LinkedList<>();

        for (MatOfDMatch matOfDMatch : knnMatches) {
            DMatch[] matches = matOfDMatch.toArray();
            if (matches.length >= 2 && matches[0].distance < 0.7 * matches[1].distance) {
                goodMatches.add(matches[0]);
            }
        }

        this.addKeyPoints(imagePoint, targetImage, keypoints1List, keypoints2List, goodMatches);

        IJ.log("[SIFT DETECTOR] End Detection.");
        knnMatches.clear();
    }
}
