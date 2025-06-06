package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.image.imagePoints.ImagePoints;
import com.ds4h.model.util.imageManager.MatImageProcessorConverter;
import ij.process.ImageProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

public class ProjectiveAlignment implements AlignmentAlgorithm{
    public static int LOWER_BOUND = 4;
    private PointOverloadEnum overload;

    /**
     * Constructor for the ProjectiveAlignment object. The point overload is initialize with "FIRST_AVAILABLE".
     */
    public ProjectiveAlignment(){
        this.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
    }

    /**
     * Aligns an input image to a target image, based on a set of corresponding points.
     * The alignment can include translation, rotation, and scaling transformations, depending on the settings (Translational Alignment only).
     * @param targetImage the target image with the desired alignment
     * @param imageToShift the input image to be aligned
     * @param ip the ImageProcessor of the input image
     * @return an AlignedImage object containing the aligned image and transformation matrix
     * @throws IllegalArgumentException if the number of points in the input images is incorrect or if the number of corners is different between images
     */
    @Override
    public AlignedImage align(ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException {
        if(targetImage.getPoints().length >= LOWER_BOUND && imageToShift.getPoints().length >= LOWER_BOUND){
            final Mat imageToShiftMat = imageToShift.getMatImage();
            final Mat alignedImage = new Mat();
            final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
            Imgproc.warpPerspective(imageToShiftMat, alignedImage, transformationMatrix, targetImage.getMatImage().size());
            return new AlignedImage(transformationMatrix, MatImageProcessorConverter.convert(alignedImage, ip), imageToShift.getName());
        }else{
            throw new IllegalArgumentException("For the Projective alignment the points must be at least: 4");
        }
    }

    /**
     * Computes and returns the transformation matrix for the given source and destination points.
     * The type of transformation performed depends on the point overload strategy chosen.
     *
     * @param srcPoints  the source points to transform
     * @param dstPoints  the corresponding destination points
     * @return           the transformation matrix computed for the given points
     * @throws IllegalArgumentException  if an invalid point overload strategy is selected or if the number of points is incorrect
     */
    @Override
    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints) {
       switch (this.getPointOverload()) {
            case FIRST_AVAILABLE:
                MatOfPoint2f newSrcPoints = new MatOfPoint2f();
                MatOfPoint2f newDstPoints = new MatOfPoint2f();
                if(srcPoints.toList().size() > this.getLowerBound()){
                    newSrcPoints.fromList(srcPoints.toList().subList(0, this.getLowerBound()));
                    newDstPoints.fromList(dstPoints.toList().subList(0, this.getLowerBound()));
                }else{
                    newSrcPoints.fromList(srcPoints.toList());
                    newDstPoints.fromList(dstPoints.toList());
                }
                return Calib3d.findHomography(newSrcPoints, newDstPoints, Calib3d.LMEDS);
            case RANSAC:
                return Calib3d.findHomography(srcPoints, dstPoints, Calib3d.RANSAC, 5);
            case MINIMUM_LAST_SQUARE:
                return Calib3d.findHomography(srcPoints, dstPoints, Calib3d.LMEDS);
        }
        throw new IllegalArgumentException("The point overload is not correct.");

    }

    /**
     * Transforms the source matrix using the homography matrix H and stores the result in the destination matrix.
     * If the number of rows in H is less than or equal to 2, the transformation is performed using affine transformation.
     * Otherwise, the transformation is performed using perspective transformation.
     *
     * @param source       the source matrix to transform
     * @param destination  the destination matrix to store the result
     * @param H            the homography matrix to use for the transformation
     */
    @Override
    public void transform(final Mat source,final Mat destination, Mat H) {
        Core.perspectiveTransform(source,destination,H);
    }

    /**
     * Returns the lower bound of the algorithm.
     * @return the lower bound, this depends on scale, rotation and selected algorithm. .
     */
    @Override
    public int getLowerBound() {
        return LOWER_BOUND;
    }

    /**
     * Set the point overload to "overload".
     * @param overload the input value selected from the user.
     */
    @Override
    public void setPointOverload(final PointOverloadEnum overload){
        this.overload = overload;
    }

    /**
     * Returns the selected point overload.
     * @return the selected point overload.
     */
    @Override
    public PointOverloadEnum getPointOverload() {
        return this.overload;
    }
}
