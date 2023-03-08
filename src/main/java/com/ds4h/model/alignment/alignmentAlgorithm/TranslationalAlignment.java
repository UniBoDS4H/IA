package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithmInterface;
import com.ds4h.model.alignment.ManualAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.ds4h.model.alignment.preprocessImage.TargetImagePreprocessing.printMat;

/**
 * This class is used for the manual alignment using the Translative technique
 */
public class TranslationalAlignment{

    public static final int LOWER_BOUND = 1;

    public TranslationalAlignment(){
    }

    public Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imageToShift) throws IllegalArgumentException{
        try {
            if(targetImage.numberOfPoints() >= LOWER_BOUND && imageToShift.numberOfPoints() >= LOWER_BOUND) {
                final Mat imageToShiftMat = imageToShift.getMatImage();
                final Point[] srcArray = imageToShift.getMatOfPoint().toArray();
                final Point[] dstArray = targetImage.getMatOfPoint().toArray();
                if(srcArray.length == dstArray.length) {
                    final Mat alignedImage = new Mat();
                    final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());//Calib3d.findHomography(targetImage.getMatOfPoint(), imageToShift.getMatOfPoint(), Calib3d.RANSAC, 5);

                    printMat(transformationMatrix);
                    if(srcArray.length <=2){//if less than 2 points mininum least square otherwise RANSAC
                       Imgproc.warpPerspective(imageToShiftMat,alignedImage,transformationMatrix, targetImage.getGrayScaleMat().size());
                     }else{
                        Imgproc.warpAffine(imageToShiftMat,alignedImage,transformationMatrix, targetImage.getMatImage().size());
                    }

                    final Optional<ImagePlus> finalImage = ImagingConversion.fromMatToImagePlus(alignedImage, imageToShift.getName());
                    return finalImage.map(imagePlus -> new AlignedImage(alignedImage, transformationMatrix, imagePlus));
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

    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints){
        if(srcPoints.toArray().length <=2){
            final Point translation = this.minimumLeastSquare(srcPoints.toArray(), dstPoints.toArray());
            final Mat translationMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
            translationMatrix.put(0, 2, translation.x);
            translationMatrix.put(1, 2, translation.y);
            return translationMatrix;
        }else{
            Mat H = Calib3d.estimateAffinePartial2D(srcPoints, dstPoints);
            H.put(0,0,1);
            H.put(0,1,0);
            H.put(1,0,0);
            H.put(1,1,1);
            return H;
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

    public void transform(final Mat source, final Mat destination, final Mat H, int nPoints){
        if(nPoints <=2){//if less than 2 points mininum least square otherwise RANSAC
            Core.perspectiveTransform(source,destination,H);
        }else{
            Core.transform(source,destination,H);
        }
    }

}
