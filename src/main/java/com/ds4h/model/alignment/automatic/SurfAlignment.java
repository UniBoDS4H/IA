package com.ds4h.model.alignment.automatic;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.alignment.manual.TranslationalAlignment;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.Pair;
import ij.IJ;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.CvType;
import org.opencv.features2d.*;
import org.opencv.xfeatures2d.SURF;
import org.opencv.imgcodecs.Imgcodecs;
import java.util.*;

/**
 * SURF alignment of images.
 * The SURF method is a fast and robust algorithm for similar images.
 */
public class SurfAlignment extends AlignmentAlgorithm {
    private static final int NUMBER_OF_ITERATION = 5;
    private final List<KeyPoint> keypoints1List, keypoints2List;
    private final List<DMatch> matchesList;
    private final List<Point> points1, points2;


    public SurfAlignment(){
        super();
        this.keypoints1List = new ArrayList<>();
        this.keypoints2List = new ArrayList<>();
        this.matchesList = new ArrayList<>();
        this.points1 = new ArrayList<>();
        this.points2 = new ArrayList<>();
    }
    /**
     * Align two images using the SURF Algorithm
     * @param targetImage : the source image for the alignment
     * @param imagePoints : the image to align
     * @return : the target image aligned to the source image
     */
    @Override
    protected Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imagePoints){

        try {
            final Mat imagePointMat = super.toGrayscale(Imgcodecs.imread(imagePoints.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
            final Mat targetImageMat = super.toGrayscale(Imgcodecs.imread(targetImage.getPath(), Imgcodecs.IMREAD_ANYCOLOR));
            final Mat H = this.getTransformationMatrix(imagePoints, targetImage);//super.traslationMatrix(imagePoints);
            this.keypoints1List.clear();
            this.keypoints2List.clear();
            this.matchesList.clear();
            //Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, SurfAlignment.NUMBER_OF_ITERATION);
            // Align the first image to the second image using the homography matrix
            return super.warpMatrix(imagePointMat, H, targetImageMat.size(), imagePoints.getFile());
        }catch (Exception e){
            IJ.showMessage(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Mat getTransformationMatrix(final ImagePoints imageToAlign, final ImagePoints targetImage) {
        this.detectPoints(imageToAlign.getMatImage(), targetImage.getMatImage());
        this.mergePoints();
        final MatOfPoint2f points1_ = new MatOfPoint2f();
        points1_.fromList(this.points1);
        final MatOfPoint2f points2_ = new MatOfPoint2f();
        points2_.fromList(this.points2);
        System.out.println(this.points1.size());
        System.out.println(this.points2.size());
        this.points1.forEach(p->imageToAlign.addPoint(p));
        this.points2.forEach(p->targetImage.addPoint(p));

        final Mat H = new TranslationalAlignment().getTransformationMatrix(imageToAlign,targetImage);
        //final Mat H = Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, SurfAlignment.NUMBER_OF_ITERATION);
        super.addMatrix(imageToAlign, H);
        return H;
    }

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H){
        Core.perspectiveTransform(source, destination, H);
    }

    private void detectPoints(final Mat imagePointMat, final Mat targetImageMat){
        this.keypoints1List.clear();
        this.keypoints2List.clear();
        this.matchesList.clear();
        final SURF detector = SURF.create();

        // Detect the keypoints and compute the descriptors for both images:
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        detector.detectAndCompute(imagePointMat , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints


        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        detector.detectAndCompute(targetImageMat, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints


        // Detect key points for the second image


        // Use the BFMatcher class to match the descriptors, BRUTE FORCE APPROACH:
        final BFMatcher matcher = BFMatcher.create();
        final MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches); // save all the matches from image1 and image2

        final MatOfDMatch matches_ = new MatOfDMatch();
        matches.convertTo(matches_, CvType.CV_32F);  // changed the datatype of the matrix from 8 bit to 32 bit floating point
        // convert the matrix of matches in to a list of DMatches, which represent the match between keypoints.
        double maxDist = 0.7;
        double minDist = 0.2;
        List<DMatch> goodMatches = new ArrayList<>();
        for (DMatch match : matches.toList()) {
            if (match.distance < maxDist * minDist) {
                goodMatches.add(match);
            }
        }
        this.matchesList.addAll(goodMatches);

        // convert the matrices of keypoints in to list of keypoints, which represent the list of keypoints in the two images
        this.keypoints1List.addAll(keypoints1.toList());
        this.keypoints2List.addAll(keypoints2.toList());
    }

    private void mergePoints(){
        /* the loop is used to iterate through the matches list, and for each match , it adds the corresponding point from the
           first image to the "points1", and the corresponding point from the second image to the "points2" list.

           The goal of this code is to extract the keypoints from the two images that were matched together and store them in two
           lists, "points1" and "points2", which will be used later by the findHomography method to compute the Homography matrix
           that aligns the two images.
        * */
        this.points1.clear();
        this.points2.clear();


        this.matchesList
                .forEach(match -> {
                    this.points1.add(this.keypoints1List.get(match.queryIdx).pt);
                    this.points2.add(this.keypoints2List.get(match.trainIdx).pt);
                });

    }
}
