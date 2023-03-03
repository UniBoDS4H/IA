package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

/**
 * This class is used for the manual alignment using the Translative technique
 */
public class TranslationalAlignment extends AlignmentAlgorithm {

    public static final int LOWER_BOUND = 1;

    public TranslationalAlignment(){
        super();
    }

    /**
     * Manual alignment using the translative alignment
     * @param targetImage : the source image used as reference
     * @param  imagePoints : the target to align
     * @throws IllegalArgumentException : in case the number of corners is not correct
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected Optional<AlignedImage> align(final ImagePoints targetImage, final ImagePoints imagePoints) throws IllegalArgumentException{
        try {
            if(targetImage.numberOfPoints() >= LOWER_BOUND && imagePoints.numberOfPoints() >= LOWER_BOUND) {
                final Mat sourceMat = targetImage.getMatImage();
                final Mat targetMat = imagePoints.getMatImage();

                final Point[] srcArray = targetImage.getMatOfPoint().toArray();
                final Point[] dstArray = imagePoints.getMatOfPoint().toArray();
                if(srcArray.length == dstArray.length) {
                    final Point translation = minimumLeastSquare(dstArray, srcArray);
                    // Shift one image by the estimated amount of translation to align it with the other
                    final Mat alignedImage = new Mat();
                    final Mat translationMatrix = Mat.eye(2, 3, CvType.CV_32FC1);
                    translationMatrix.put(0, 2, translation.x);
                    translationMatrix.put(1, 2, translation.y);
                    Imgproc.warpAffine(targetMat, alignedImage, translationMatrix, targetMat.size());
                    final Optional<ImagePlus> finalImage = this.convertToImage(imagePoints.getFile(), alignedImage);
                    System.out.println(alignedImage);
                    return finalImage.map(imagePlus -> new AlignedImage(alignedImage, translationMatrix, imagePlus));
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

    private Point minimumLeastSquare(final Point[] srcArray, final Point[] dstArray){
        final double[] deltaX = new double[srcArray.length];
        final double[] deltaY = new double[srcArray.length];

        for (int i = 0; i < srcArray.length; i++) {
            deltaX[i] = dstArray[i].x - srcArray[i].x;
            deltaY[i] = dstArray[i].y - srcArray[i].y;
            System.out.println(deltaX[i] + " " + deltaY[i]);
        }

        final double meanDeltaX = Core.mean(new MatOfDouble(deltaX)).val[0];
        final double meanDeltaY = Core.mean(new MatOfDouble(deltaY)).val[0];
        return new Point(meanDeltaX, meanDeltaY);
    }

}
