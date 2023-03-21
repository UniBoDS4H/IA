package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * This class is used for the manual alignment using the Translative technique
 */
public class TranslationalAlignment implements AlignmentAlgorithm {

    public static int LOWER_BOUND = 1;
    private boolean rotate;
    private boolean scale;
    private boolean translate;

    public TranslationalAlignment(){
        this.setTransformation(true, false,false);
    }
    public boolean getTranslate(){
        return this.translate;
    }
    public boolean getRotate(){
        return this.rotate;
    }
    public boolean getScale(){
        return this.scale;
    }
    @Override
    public Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException{
        try {
            if(targetImage.numberOfPoints() >= LOWER_BOUND && imageToShift.numberOfPoints() >= LOWER_BOUND) {
                if(imageToShift.numberOfPoints() == targetImage.numberOfPoints()) {
                    final Mat alignedImage = new Mat();
                    final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
                    if(imageToShift.numberOfPoints() <=2){//if less than 2 points mininum least square otherwise RANSAC
                        IJ.log("[TRANSLATIONAL ALIGNMENT] Starting the warpPerspective");
                        System.gc();
                        Imgproc.warpPerspective(imageToShift.getMatImage(), alignedImage, transformationMatrix, targetImage.getMatImage().size());
                     }else{
                        IJ.log("[TRANSLATIONAL ALIGNMENT] Starting the warpAffine");
                        System.gc();
                        Imgproc.warpAffine(imageToShift.getMatImage(), alignedImage, transformationMatrix, targetImage.getMatImage().size());
                    }
                    System.gc();
                    return Optional.of(new AlignedImage(transformationMatrix, ImagingConversion.matToImagePlus(alignedImage, imageToShift.getName(), ip)));
                }else{
                    throw new IllegalArgumentException("The number of corner inside the source image is different from the number of points" +
                            "inside the target image.");
                }
            }else{
                throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                        "In order to use the Translation alignment you must at least: " + TranslationalAlignment.LOWER_BOUND + " points.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
    public void setTransformation(boolean translate,boolean rotate, boolean scale){
        this.translate = translate;
        this.rotate = rotate;
        this.scale = scale;
    }
    @Override
    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints){
        IJ.log("[TRANSLATIONAL ALIGNMENT] Source Points: " + srcPoints.toList().size() + ", Destination Points: " + dstPoints.toList().size());
        if(srcPoints.toArray().length <=2){
            final Point translation = this.minimumLeastSquare(srcPoints.toArray(), dstPoints.toArray());
            final Mat translationMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
            translationMatrix.put(0, 2, translation.x);
            translationMatrix.put(1, 2, translation.y);
            return translationMatrix;
        }else{
            final Mat H = Calib3d.estimateAffinePartial2D(srcPoints, dstPoints);
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

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H, int nPoints){
        if(nPoints <=2){//if less than 2 points mininum least square otherwise RANSAC
            Core.perspectiveTransform(source,destination,H);
        }else{
            Core.transform(source,destination,H);
        }
    }

    @Override
    public int getLowerBound() {
        return LOWER_BOUND;
    }

}
