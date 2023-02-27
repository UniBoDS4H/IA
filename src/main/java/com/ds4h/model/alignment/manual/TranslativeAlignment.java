package com.ds4h.model.alignment.manual;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.alignment.AlignmentAlgorithm;
import com.ds4h.model.imageCorners.ImageCorners;
import ij.IJ;
import ij.ImagePlus;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

public class TranslativeAlignment extends AlignmentAlgorithm {

    private static final int LOWER_BOUND = 1;

    public TranslativeAlignment(){
        super();
    }

    /**
     * Manual alignment using the translative alignment
     * @param source : the source image used as reference
     * @param  target : the target to align
     * @throws IllegalArgumentException : in case the number of corners is not correct
     * @return : the list of all the images aligned to the source
     */
    @Override
    protected Optional<AlignedImage> align(final ImageCorners source, final ImageCorners target) throws IllegalArgumentException{
        try {
            if(source.numberOfCorners() >= LOWER_BOUND && target.numberOfCorners() >= LOWER_BOUND) {
                final Mat sourceMat = source.getMatImage();
                final Mat targetMat = target.getMatImage();



                final Point[] srcArray = source.getMatOfPoint().toArray();
                final Point[] dstArray = target.getMatOfPoint().toArray();
                final double[] deltaX = new double[srcArray.length];
                final double[] deltaY = new double[srcArray.length];
                //TODO: Check if all have the same points
                for (int i = 0; i < srcArray.length; i++) {
                    System.out.println(srcArray[i]);
                    deltaX[i] = dstArray[i].x - srcArray[i].x;
                    deltaY[i] = dstArray[i].y - srcArray[i].y;
                }

                final double meanDeltaX = Core.mean(new MatOfDouble(deltaX)).val[0];
                final double meanDeltaY = Core.mean(new MatOfDouble(deltaY)).val[0];
                final Point translation = new Point(meanDeltaX, meanDeltaY);
                // Shift one image by the estimated amount of translation to align it with the other
                final Mat alignedImage = new Mat(targetMat.rows(), targetMat.cols(), targetMat.type());
                final Mat translationMatrix = Mat.eye(2, 3, CvType.CV_32FC1);
                translationMatrix.put(0, 2, translation.x);
                translationMatrix.put(1, 2, translation.y);
                Imgproc.warpAffine(targetMat, alignedImage, translationMatrix, sourceMat.size());
                final Optional<ImagePlus> finalImage = this.convertToImage(target.getFile(), alignedImage);
                System.out.println(alignedImage);
                return finalImage.map(imagePlus -> new AlignedImage(alignedImage, translationMatrix ,imagePlus));
            }else{
                throw new IllegalArgumentException("The number of corners inside the source image or inside the target image is not correct.\n" +
                        "In order to use the Affine alignment you must at least: " + TranslativeAlignment.LOWER_BOUND + " corners.");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
}
