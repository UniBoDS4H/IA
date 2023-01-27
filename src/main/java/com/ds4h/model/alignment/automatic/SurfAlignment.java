package com.ds4h.model.alignment.automatic;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.ImagingConversion;
import com.ds4h.model.util.NameBuilder;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SURF;

import java.io.File;
import java.util.*;

/**
 * SURF alignment of images.
 * The SURF method is a fast and robust algorithm for similar images.
 */
public class SurfAlignment {
    private static final int NUMBER_OF_ITERATION = 5;
    static {
        // Load the library for the alignment
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Align two images using the SURF Algorithm
     * @param sourceImage : the source image for the alignment
     * @param targetImage : the image to align
     * @return : the target image aligned to the source image
     */
    private static Optional<ImagePlus> align(final ImageCorners sourceImage, final ImageCorners targetImage){
        //Read the two images you want to align using the Imgcodecs class:
        final Mat image1 = Imgcodecs.imread(sourceImage.getPath(), Imgcodecs.IMREAD_GRAYSCALE);
        final Mat image2 = Imgcodecs.imread(targetImage.getPath(), Imgcodecs.IMREAD_GRAYSCALE);


        // Detect keypoints and compute descriptors using the SURF algorithm
        final SURF detector = SURF.create();

        // Detect the keypoints and compute the descriptors for both images:
        final MatOfKeyPoint keypoints1 = new MatOfKeyPoint(); // Matrix where are stored all the key points
        final Mat descriptors1 = new Mat();
        detector.detectAndCompute(image1 , new Mat(), keypoints1, descriptors1); // Detect and save the keypoints

        // Detect key points for the second image
        final MatOfKeyPoint keypoints2 = new MatOfKeyPoint(); //  Matrix where are stored all the key points
        final Mat descriptors2 = new Mat();
        detector.detectAndCompute(image2, new Mat(), keypoints2, descriptors2); // Detect and save the keypoints


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
        /* the loop is used to iterate through the matches list, and for each match , it adds the corresponding point from the
           first image to the "points1", and the corresponding point from the second image to the "points2" list.

           The goal of this code is to extract the keypoints from the two images that were matched together and store them in two
           lists, "points1" and "points2", which will be used later by the findHomography method to compute the Homography matrix
           that aligns the two images.
        * */
        for (DMatch match : matchesList) {
            /*EXPLANATION :
                matchesList.get(i) : get the i-th element of the matchesList, which is s DMatch object representing a match between two keypoints
                .queryIdx : is a property of the DMatch that represents the index of the keypoint in the query image(the first image passed to the BFMatcher)
                .pt : is a property of the keypoint object that represents the 2D point in the image that corresponding to the keypoint
            */
            // Adds the point from the query image that corresponding to the current match to the "points1" list.
            points1.add(keypoints1List.get(match.queryIdx).pt);
        }

        /*
            The goal of this code is to extract the keypoints from the two images that were matched together and store them in two lists
            "points1" and "points2", which will be used later by the findHomography method to compute the Homography matrix that aligns
            the two images
         */
        final List<Point> points2 = new ArrayList<>();
        for (DMatch dMatch : matchesList) {
            /*EXPLANATION :
                matchesList.get(i) : get the i-th element of the matchesList, which is s DMatch object representing a match between two keypoints
                .trainIdx : is a property of the DMatch object that represents the index of the keypoint in the train image(the second image passed to the BFMatcher)
                .pt : is a property of the keypoint object that represents the 2D point in the image that corresponding to the keypoint
            */
            points2.add(keypoints2List.get(dMatch.trainIdx).pt);
        }

        final MatOfPoint2f points1_ = new MatOfPoint2f();
        points1_.fromList(points1);
        final MatOfPoint2f points2_ = new MatOfPoint2f();
        points2_.fromList(points2);

        // Compute the homography matrix that aligns two images.
        /*
            points1_ : a matrix of 2D points in the first image (query image)
            points2_ : a matrix of 2D points in the second image (train image)
            Calib3d.RANSAC : the algorithm used to compute the Homography.
            NUMBER_OF_ITERATION : number of iteration for the RANSAC algorithm
         */
        final Mat H = Calib3d.findHomography(points1_, points2_, Calib3d.RANSAC, SurfAlignment.NUMBER_OF_ITERATION);

        final Mat alignedImage1 = new Mat();
        // Align the first image to the second image using the homography matrix
        Imgproc.warpPerspective(image1, alignedImage1, H, image2.size());
        return SurfAlignment.convertToImage(targetImage.getFile(), alignedImage1);
    }

    /**
     * Align the images stored inside the cornerManager. All the images will be aligned to the source image
     * @param cornerManager : container where all the images are stored
     * @return the List of all the images aligned to the source
     */
    public static List<ImagePlus> alignImages(final CornerManager cornerManager){
        final List<ImagePlus> images = new LinkedList<>();
        if(Objects.nonNull(cornerManager) && cornerManager.getSourceImage().isPresent()) {
            final ImageCorners source = cornerManager.getSourceImage().get();
            cornerManager.getCornerImagesImages().forEach(image -> SurfAlignment.align(source, image).ifPresent(images::add));
        }
        return images;
    }

    /**
     * Convert the new matrix in to an image
     * @param file : this will be used in order to get the name and store used it for the creation of the new file.
     * @param matrix : the image aligned matrix
     * @return : the new image created by the Matrix.
     */
    private static Optional<ImagePlus> convertToImage(final File file, final Mat matrix){
        return ImagingConversion.fromMatToImagePlus(matrix, file.getName(), NameBuilder.DOT_SEPARATOR);
    }

}
