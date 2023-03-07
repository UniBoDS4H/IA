package com.ds4h.model.alignment.semiAutomatic;


import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
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
/*
public class SemiAutomaticAlignment extends AlignmentAlgorithm {

    private final List<Point> points1, points2;

    public SemiAutomaticAlignment(){
        super();
        this.points1 = new ArrayList<>();
        this.points2 = new ArrayList<>();
    }

    @Override
    public Optional<AlignedImage> align(final List<Point> targetImage, final ImagePoints imagePoints, Size targetSize){
        try {
            if(targetImage.size() == imagePoints.numberOfPoints()) {
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


                for (DMatch match : matchesList) {
                    points1.add(keypoints1List.get(match.queryIdx).pt);
                }
                for (DMatch dMatch : matchesList) {
                    points2.add(keypoints2List.get(dMatch.trainIdx).pt);
                }
                this.points1.addAll(targetImage.getListPoints());
                this.points2.addAll(imagePoints.getListPoints());
                //********
                //final MatOfPoint2f points1_ = targetImage.getMatOfPoint()
                //points1_.fromArray(points1);
                //final MatOfPoint2f points2_ = imagePoints.getMatOfPoint()
                //points2_.fromArray(points1);
                final Mat homography = this.getTransformationMatrix(imagePoints, targetImage);
                //Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, 5);
                // Compute translation matrix from homography matrix
                //*******
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
    public Mat getTransformationMatrix(final ImagePoints imageToAlign, final ImagePoints targetImage) {
        final MatOfPoint2f points1_ = new MatOfPoint2f();
        points1_.fromList(this.points1);
        final MatOfPoint2f points2_ = new MatOfPoint2f();
        points2_.fromList(this.points2);
        return Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, 5);
    }

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H){
        Core.perspectiveTransform(source, destination, H);
    }
}
 */
