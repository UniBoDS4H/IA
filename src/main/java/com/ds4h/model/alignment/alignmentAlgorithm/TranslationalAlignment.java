package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.image.imagePoints.ImagePoints;
import com.ds4h.model.util.imageManager.MatImageProcessorConverter;
import ij.IJ;
import ij.process.ImageProcessor;
import org.jetbrains.annotations.NotNull;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This class is used for the manual alignment using the Translation technique.
 */
public class TranslationalAlignment implements AlignmentAlgorithm {

    public static int LOWER_BOUND = 1;
    private boolean rotate;
    private boolean scale;
    private boolean translate;
    private PointOverloadEnum overload;

    /**
     * Constructor for the TranslationalAlignment object. All the flags are set to false except for the
     * "translate" flag. The point overload is initialize with "FIRST_AVAILABLE"
     */
    public TranslationalAlignment(){
        this.setTransformation(true, false,false);
        this.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
    }

    /**
     * Returns "True" if translate is selected, otherwise "False".
     * @return if the translation flag is selected.
     */
    public boolean getTranslate(){
        return this.translate;
    }

    /**
     * Returns "True" if rotate is selected, otherwise "False".
     * @return if the rotate flag is selected.
     */
    public boolean getRotate(){
        return this.rotate;
    }

    /**
     * Returns "True" if scale is selected otherwise "False".
     * @return if the scale flag is selected.
     */
    public boolean getScale(){
        return this.scale;
    }

    /**
     * Set the point overload to "overload".
     * @param overload the input value selected from the user.
     */
    @Override
    public void setPointOverload(final PointOverloadEnum overload){
        if(Objects.nonNull(overload)) {
            this.overload = overload;
        }
    }

    /**
     * Returns the selected point overload.
     * @return the selected point overload.
     */
    @Override
    public PointOverloadEnum getPointOverload() {
        return this.overload;
    }

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
    @Override
    public @NotNull AlignedImage align(final @NotNull ImagePoints targetImage, final @NotNull ImagePoints imageToShift, @NotNull ImageProcessor ip) throws IllegalArgumentException{
        if(targetImage.numberOfPoints() >= this.getLowerBound() && imageToShift.numberOfPoints() >= this.getLowerBound()) {
            if(imageToShift.numberOfPoints() == targetImage.numberOfPoints()) {
                final Mat alignedImage = new Mat();
                final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
                if(transformationMatrix.rows() <=2) {
                    IJ.log("[TRANSLATIONAL ALIGNMENT] Starting the warpAffine");
                    IJ.log("[TRANSLATIONAL ALIGNMENT] Target Size: " + targetImage.getMatSize());
                    System.gc();
                    Imgproc.warpAffine(imageToShift.getMatImage(), alignedImage, transformationMatrix, targetImage.getMatSize());
                }
                else{
                    IJ.log("[TRANSLATIONAL ALIGNMENT] Starting the warpPerspective");
                    IJ.log("[TRANSLATIONAL ALIGNMENT] Target Size: " + targetImage.getMatSize());
                    System.gc();
                    Imgproc.warpPerspective(imageToShift.getMatImage(), alignedImage, transformationMatrix, targetImage.getMatSize());
                }
                IJ.log("[TRANSLATIONAL] Final matrix: " + alignedImage);
                final AlignedImage finalImg = new AlignedImage(transformationMatrix,
                        MatImageProcessorConverter.convert(alignedImage, ip),
                        imageToShift.getName());
                ip = null;
                System.gc();
                return finalImg;
            }else{
                throw new IllegalArgumentException("The number of corner inside the source image is different from the number of points" +
                        "inside the target image.");
            }
        }else{
            throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                    "In order to use the Translation alignment" + (this.scale?" with scaling ":"") + (this.rotate?" with rotation ":"") +" you must at least: " + this.getLowerBound() + " points.");
        }
    }


    /**
     * Sets the types of transformation to be applied during alignment.
     * @param translate a boolean value indicating whether translation transformation is enabled or not
     * @param rotate a boolean value indicating whether rotation transformation is enabled or not
     * @param scale a boolean value indicating whether scaling transformation is enabled or not
     */
    public void setTransformation(boolean translate,boolean rotate, boolean scale){
        this.translate = translate;
        this.rotate = rotate;
        this.scale = scale;
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
    @NotNull
    @Override
    public Mat getTransformationMatrix(@NotNull final MatOfPoint2f srcPoints, @NotNull final MatOfPoint2f dstPoints){
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
                if(this.getLowerBound() == 1){
                    final Point translation = this.minimumLeastSquare(newSrcPoints.toArray(), newDstPoints.toArray());
                    final Mat translationMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
                    translationMatrix.put(0, 2, translation.x);
                    translationMatrix.put(1, 2, translation.y);
                    return translationMatrix;
                }else if(this.getLowerBound() > 2){
                    return getMatWithTransformation(Calib3d.estimateAffinePartial2D(newSrcPoints,newDstPoints));
                }else{
                    throw new IllegalArgumentException("the number of point is not correct");
                }
            case RANSAC:
                return getMatWithTransformation(Calib3d.estimateAffinePartial2D(srcPoints, dstPoints, new Mat(), Calib3d.RANSAC, 5, 2000, 0.99));
            case MINIMUM_LAST_SQUARE:
                if(srcPoints.toList().size() < 3){
                    final Point translation = this.minimumLeastSquare(srcPoints.toArray(), dstPoints.toArray());
                    final Mat translationMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
                    translationMatrix.put(0, 2, translation.x);
                    translationMatrix.put(1, 2, translation.y);
                    return translationMatrix;
                }else{
                    return getMatWithTransformation(Calib3d.estimateAffinePartial2D(srcPoints, dstPoints, new Mat(), Calib3d.LMEDS));
                }
        }
        throw new IllegalArgumentException("bad point overload selected");
    }

    private Mat getMatWithTransformation(Mat H) {
        IJ.log("[TRANSLATIONAL] Matrix: " + H);
        double a = H.get(0,0)[0];
        double b = H.get(1,0)[0];
        double scaling = Math.sqrt(a*a + b*b);
        double theta = Math.acos(a/scaling);
        double x = H.get(0,2)[0];
        double y = H.get(1,2)[0];
        Mat transformation = Mat.eye(2,3,H.type());
        if(this.translate){
            IJ.log("[TRANSLATIONAL ALIGNMENT] Translate");
            transformation.put(0,2, x);
            transformation.put(1,2, y);
        }
        if(this.scale){
            IJ.log("[TRANSLATIONAL ALIGNMENT] Scale");
            transformation.put(0,0,scaling);
            transformation.put(0,1,scaling);
            transformation.put(1,0,scaling);
            transformation.put(1,1,scaling);
        }else{
            transformation.put(0,0,1);
            transformation.put(0,1,1);
            transformation.put(1,0,1);
            transformation.put(1,1,1);
        }
        if(this.rotate){
            IJ.log("[TRANSLATIONAL ALIGNMENT] Rotate");
            transformation.put(0,0,transformation.get(0,0)[0]*Math.cos(theta));
            transformation.put(0,1,transformation.get(0,1)[0]*(-Math.sin(theta)));
            transformation.put(1,0,transformation.get(1,0)[0]*Math.sin(theta));
            transformation.put(1,1,transformation.get(1,1)[0]*Math.cos(theta));
        }else{
            transformation.put(0,1,0);
            transformation.put(1,0,0);
        }
        return transformation;
    }


    private Point minimumLeastSquare(final Point[] srcArray, final Point[] dstArray){
        final double[] deltaX = new double[srcArray.length];
        final double[] deltaY = new double[srcArray.length];

        IntStream.range(0, srcArray.length).parallel().forEach(i -> {
            deltaX[i] = dstArray[i].x - srcArray[i].x;
            deltaY[i] = dstArray[i].y - srcArray[i].y;
        });

        final double meanDeltaX = Core.mean(new MatOfDouble(deltaX)).val[0];
        final double meanDeltaY = Core.mean(new MatOfDouble(deltaY)).val[0];
        return new Point(meanDeltaX, meanDeltaY);
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
    public void transform(final @NotNull Mat source, final @NotNull Mat destination, final @NotNull Mat H){
        if(H.rows() <=2){
            Core.transform(source,destination,H);
        }else{
            Core.perspectiveTransform(source,destination,H);
        }
    }

    /**
     * Returns the lower bound of the algorithm.
     * @return the lower bound, this depends on scale, rotation and selected algorithm. .
     */
    @Override
    public int getLowerBound() {
        return LOWER_BOUND + (this.scale || this.rotate? 2 : 0);
    }

}
