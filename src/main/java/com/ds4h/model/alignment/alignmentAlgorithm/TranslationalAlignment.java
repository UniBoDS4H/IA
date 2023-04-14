package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.imageManager.MatImageProcessorConverter;
import ij.IJ;
import ij.process.ImageProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import java.util.stream.IntStream;

/**
 * This class is used for the manual alignment using the Translative technique
 */
public class TranslationalAlignment implements AlignmentAlgorithm {

    public static int LOWER_BOUND = 1;
    private boolean rotate;
    private boolean scale;
    private boolean translate;
    private PointOverloadEnum overload;

    /**
     *
     */
    public TranslationalAlignment(){
        this.setTransformation(true, false,false);
        this.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
    }

    /**
     *
     * @return a
     */
    public boolean getTranslate(){
        return this.translate;
    }

    /**
     *
     * @return a
     */
    public boolean getRotate(){
        return this.rotate;
    }

    /**
     *
     * @return a
     */
    public boolean getScale(){
        return this.scale;
    }

    /**
     *
     * @param overload a
     */
    @Override
    public void setPointOverload(PointOverloadEnum overload){
        this.overload = overload;
    }

    /**
     *
     * @return b
     */
    @Override
    public PointOverloadEnum getPointOverload() {
        return this.overload;
    }

    /**
     *
     * @param targetImage a
     * @param imageToShift b
     * @param ip c
     * @return d
     * @throws IllegalArgumentException e
     */
    @Override
    public AlignedImage align(final ImagePoints targetImage, final ImagePoints imageToShift, ImageProcessor ip) throws IllegalArgumentException{
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
     *
     * @param translate a
     * @param rotate b
     * @param scale c
     */
    public void setTransformation(boolean translate,boolean rotate, boolean scale){
        this.translate = translate;
        this.rotate = rotate;
        this.scale = scale;
    }

    /**
     *
     * @param srcPoints a
     * @param dstPoints b
     * @return c
     */
    @Override
    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints){
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

    /**
     *
     * @param H a
     * @return b
     */
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
     *
     * @param source a
     * @param destination b
     * @param H c
     */
    @Override
    public void transform(final Mat source, final Mat destination, final Mat H){
        if(H.rows() <=2){
            Core.transform(source,destination,H);
        }else{
            Core.perspectiveTransform(source,destination,H);
        }
    }

    /**
     *
     * @return a
     */
    @Override
    public int getLowerBound() {
        return LOWER_BOUND + (this.scale || this.rotate? 2 : 0);
    }

}
