package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.image.imagePoints.ImagePoints;
import ij.process.ImageProcessor;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;

public interface AlignmentAlgorithm {

    /**
     * Aligns an input image to a target image, based on a set of corresponding points.
     * The alignment can include translation, rotation, and scaling transformations, depending on the settings (Translational Alignment only).
     *
     * @param targetImage  the target image with the desired alignment
     * @param imageToShift the input image to be aligned
     * @param ip           the ImageProcessor of the input image
     * @return an AlignedImage object containing the aligned image and transformation matrix
     * @throws IllegalArgumentException if the number of points in the input images is incorrect or if the number of corners is different between images
     */
    @NotNull
    AlignedImage align(@NotNull final ImagePoints targetImage,
                       @NotNull final ImagePoints imageToShift,
                       @NotNull final ImageProcessor ip) throws IllegalArgumentException;

    /**
     * Computes and returns the transformation matrix for the given source and destination points.
     * The type of transformation performed depends on the point overload strategy chosen.
     *
     * @param srcPoints  the source points to transform
     * @param dstPoints  the corresponding destination points
     * @return           the transformation matrix computed for the given points
     * @throws IllegalArgumentException  if an invalid point overload strategy is selected or if the number of points is incorrect
     */
    @NotNull
    Mat getTransformationMatrix(@NotNull final MatOfPoint2f srcPoints,
                                @NotNull final MatOfPoint2f dstPoints);

    /**
     * Transforms the source matrix using the homography matrix H and stores the result in the destination matrix.
     * If the number of rows in H is less than or equal to 2, the transformation is performed using affine transformation.
     * Otherwise, the transformation is performed using perspective transformation.
     *
     * @param source       the source matrix to transform
     * @param destination  the destination matrix to store the result
     * @param H            the homography matrix to use for the transformation
     */
    void transform(@NotNull final Mat source,
                   @NotNull final Mat destination,
                   @NotNull Mat H);

    /**
     * Returns the lower bound of the algorithm.
     * @return the lower bound, this depends on scale, rotation and selected algorithm. .
     */
    int getLowerBound();

    /**
     * Set the point overload to "overload".
     * @param overload the input value selected from the user.
     */
    void setPointOverload(@NotNull PointOverloadEnum overload);

    /**
     * Returns the selected point overload.
     * @return the selected point overload.
     */
    @NotNull
    PointOverloadEnum getPointOverload();
}
