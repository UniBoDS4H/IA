package com.ds4h.model.alignment.automatic.pointDetector.siftDetector;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.converter.ImagePlusMatConverter;
import ij.IJ;
import ij.process.ImageProcessor;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.SIFT;

import java.util.List;

public class SIFTDetector extends PointDetector {
    private final SIFT sift = SIFT.create();
    private final DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
    private static final double THRESHOLD = 300;


    @Override
    public void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor) {
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        final ImageProcessor az = this.createPyramid(imagePoint, 4).getProcessor();
        final Mat grayImg = ImagePlusMatConverter.convertGray(az);
        final Mat grayTarget = ImagePlusMatConverter.convertGray(this.createPyramid(targetImage, 4).getProcessor());
        grayImg.convertTo(grayImg, CvType.CV_8U);
        grayTarget.convertTo(grayTarget, CvType.CV_8U);
        IJ.log("[SIFT] : " + grayImg);
        final Mat descriptors1 = new Mat();
        final Mat descriptors2 = new Mat();
        //sift.detectAndCompute(imagePoint.getGrayScaleMat(), new Mat(), keypoints1, descriptors1);

        /*
        Imgproc.resize(imagePoint.getGrayScaleMat(), grayImg,
                new Size(imagePoint.getWidth()*0.25,
                imagePoint.getHeight()*0.25));


        Imgproc.resize(targetImage.getGrayScaleMat(), grayTarget,
                new Size(imagePoint.getWidth()*0.25,
                        imagePoint.getHeight()*0.25));

         */
        //MatImagePlusConverter.convert(grayImg, "TITOLO", new ByteProcessor(0,0)).show();
        sift.detectAndCompute(grayImg, new Mat(), keypoints1, descriptors1);
        IJ.log("[SIFT DETECTOR] Detected points for the first image.");

        System.gc();
        grayImg.release();

        sift.detectAndCompute(grayTarget, new Mat(), keypoints2, descriptors2);
        IJ.log("[SIFT DETECTOR] Detected points for the target image.");

        System.gc();
        grayTarget.release();
        final MatOfDMatch matches = new MatOfDMatch();
        this.matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2

        IJ.log("[SIFT DETECTOR] Points: " + matches);
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
        final List<KeyPoint> keypoints1List = keypoints1.toList();
        final List<KeyPoint> keypoints2List = keypoints2.toList();
        matches.toList().stream()
                .filter(match -> match.distance < threshold)
                .forEach(goodMatch -> {

                    final Point queryScaled = new Point(keypoints1List.get(goodMatch.queryIdx).pt.x * (1/0.25),
                            keypoints1List.get(goodMatch.queryIdx).pt.y * (1/0.25));

                    final Point trainScaled = new Point(keypoints2List.get(goodMatch.trainIdx).pt.x * (1/0.25),
                            keypoints2List.get(goodMatch.trainIdx).pt.y * (1/0.25));

                    imagePoint.addPoint(queryScaled);
                    targetImage.addPoint(trainScaled);
                });
        IJ.log("[SIFT DETECTOR] End Detection.");
    }
}
