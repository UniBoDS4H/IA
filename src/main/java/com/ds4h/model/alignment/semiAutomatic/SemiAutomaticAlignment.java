package com.ds4h.model.alignment.semiAutomatic;


import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SURF;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SemiAutomaticAlignment extends AlignmentAlgorithm {

    public SemiAutomaticAlignment(){
        super();
    }

    @Override
    protected Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imagePoints){
        try {
            if(targetImage.numberOfPoints() == imagePoints.numberOfPoints()) {
                //************************
                final Mat imagePointMat = super.toGrayscale(Imgcodecs.imread(imagePoints.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
                final Mat targetImageMat = super.toGrayscale(Imgcodecs.imread(targetImage.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
                // Detect keypoints and compute descriptors using the SURF algorithm
                final SURF detector = SURF.create();

                // Detect the keypoints and compute the descriptors for both images:
                final MatOfKeyPoint keypoints1 = imagePoints.getMatOfKeyPoint(); // Matrix where are stored all the key points
                final Mat descriptors1 = new Mat();
                detector.detectAndCompute(imagePointMat , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints

                // Detect key points for the second image
                final MatOfKeyPoint keypoints2 = targetImage.getMatOfKeyPoint(); //  Matrix where are stored all the key points
                final Mat descriptors2 = new Mat();
                detector.detectAndCompute(targetImageMat, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints

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
                for (DMatch match : matchesList) {
                    points1.add(keypoints1List.get(match.queryIdx).pt);
                }
                final List<Point> points2 = new ArrayList<>();
                for (DMatch dMatch : matchesList) {
                    points2.add(keypoints2List.get(dMatch.trainIdx).pt);
                }

                final MatOfPoint2f points1_ = targetImage.getMatOfPoint();
                points1_.fromList(points1);
                final MatOfPoint2f points2_ = imagePoints.getMatOfPoint();
                points2_.fromList(points2);
                final Mat homography = Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, 5);
                // Compute translation matrix from homography matrix
                double tx = homography.get(0, 2)[0];
                double ty = homography.get(1, 2)[0];
                final Mat translationMatrix = new Mat(2, 3, CvType.CV_64F);
                translationMatrix.put(0, 0, 1);
                translationMatrix.put(0, 2, tx);
                translationMatrix.put(1, 1, 1);
                translationMatrix.put(1, 2, ty);
                //************************

                final Mat warpedMat = new Mat();
                Imgproc.warpAffine(imagePoints.getMatImage(), warpedMat, translationMatrix, targetImage.getMatImage().size());
                final Optional<ImagePlus> finalImage = this.convertToImage(imagePoints.getFile(), warpedMat);
                return finalImage.map(imagePlus -> new AlignedImage(warpedMat, translationMatrix, imagePlus));
            }
        }catch (Exception e){
            IJ.showMessage(e.getMessage());
        }
        return Optional.empty();
    }
    private List<KeyPoint> addKeyPoints(final MatOfKeyPoint keyPoint, final ImagePoints image){
        final MatOfKeyPoint imageKeyPoint = image.getMatOfKeyPoint();
        return imageKeyPoint.toList();
    }

    @Override
    public Mat getTransformationMatrix(Point[] dstArray, Point[] srcArray) {
        return null;
    }
}
