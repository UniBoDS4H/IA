package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.ManualAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * This class is used for the manual alignment using the Translative technique
 */
public class TranslationalAlignment extends ManualAlgorithm {

    public static final int LOWER_BOUND = 1;

    public TranslationalAlignment(){
        super();
    }

    /**
     * Manual alignment using the translative alignment
     *
     * @param imagePoints : the target to align
     * @param targetSize
     * @return : the list of all the images aligned to the source
     * @throws IllegalArgumentException : in case the number of corners is not correct
     */
    @Override
    public Optional<AlignedImage> align(final MatOfPoint2f targetPoints, final ImagePoints imagePoints, Size targetSize) throws IllegalArgumentException{
        try {
            if(targetPoints.toList().size() >= LOWER_BOUND && imagePoints.numberOfPoints() >= LOWER_BOUND) {
                final Mat imageToShiftMat = imagePoints.getMatImage();

                final Point[] srcArray = imagePoints.getMatOfPoint().toArray();
                final Point[] dstArray = targetPoints.toArray();
                if(srcArray.length == dstArray.length) {
                    final Mat alignedImage = new Mat();
                    final Point translation = minimumLeastSquare(srcArray, dstArray);
                    final Mat translationMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
                    translationMatrix.put(0, 2, translation.x);
                    translationMatrix.put(1, 2, translation.y);
                    //final Mat translationMatrix = this.getTransformationMatrix(imagePoints, targetImage);//super.traslationMatrix(imagePoints);
                    Imgproc.warpPerspective(imageToShiftMat, alignedImage, translationMatrix, targetSize);
                    final Optional<ImagePlus> finalImage = this.convertToImage(imagePoints.getFile(), alignedImage);
                    return finalImage.map(imagePlus -> new AlignedImage(alignedImage, translationMatrix, imagePlus));
                }else{
                    throw new IllegalArgumentException("The number of corner inside the source image is different from the number of points" +
                            "inside the target image.");
                }
            }else{
                System.out.println("INSIDE T: " + imagePoints.getMatOfPoint());
                System.out.println("INSIDE T: " + targetPoints);
                throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                        "In order to use the Translation alignment you must at least: " + TranslationalAlignment.LOWER_BOUND + " points.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public Mat getTransformationMatrix(final ImagePoints imageToAlign, final ImagePoints targetImage){
        final Point translation = minimumLeastSquare(imageToAlign.getPoints(), targetImage.getPoints());
        final Mat translationMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
        translationMatrix.put(0, 2, translation.x);
        translationMatrix.put(1, 2, translation.y);
        return translationMatrix;
    }


    private static Point minimumLeastSquare(final Point[] srcArray, final Point[] dstArray){
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
    public void transform(final Mat source, final Mat destination, final Mat H){
        Core.perspectiveTransform(source, destination, H);
    }

}
