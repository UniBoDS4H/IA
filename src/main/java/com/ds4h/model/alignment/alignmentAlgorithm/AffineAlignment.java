package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.imageManager.MatImageProcessorConverter;
import ij.process.ImageProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

public class AffineAlignment implements AlignmentAlgorithm{
    public static int LOWER_BOUND = 3;
    private PointOverloadEnum overload;
    public AffineAlignment(){
        this.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
    }

    @Override
    public AlignedImage align(final ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException {
        if(targetImage.numberOfPoints() >= LOWER_BOUND && imageToShift.numberOfPoints() >= LOWER_BOUND) {
            final Mat imageToShiftMat = imageToShift.getMatImage();
                final Mat alignedImage = new Mat();
                final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
                Imgproc.warpAffine(imageToShiftMat, alignedImage, transformationMatrix, targetImage.getMatImage().size());
                return new AlignedImage(transformationMatrix, MatImageProcessorConverter.convert(alignedImage, ip), imageToShift.getName());
        }else {
            throw new IllegalArgumentException("The number of points inside the source image or inside the target image is not correct.\n" +
                    "In order to use the Affine alignment you must use: " + AffineAlignment.LOWER_BOUND + " points.");
        }
    }

    @Override
    public Mat getTransformationMatrix(MatOfPoint2f srcPoints, MatOfPoint2f dstPoints) {
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
                return Calib3d.estimateAffine2D(newSrcPoints, newDstPoints, new Mat(), Calib3d.LMEDS);
            case RANSAC:
                return Calib3d.estimateAffine2D(srcPoints, dstPoints, new Mat(), Calib3d.RANSAC, 5, 2000, 0.99);
            case MINIMUM_LAST_SQUARE:
                return Calib3d.estimateAffine2D(srcPoints, dstPoints, new Mat(), Calib3d.LMEDS);
        }
        throw new IllegalArgumentException("The point overload is not correct.");
    }

    @Override
    public void transform(final Mat source, final Mat destination, final Mat H) {
        /*
        if(H.rows() <=2){
            Core.transform(source,destination,H);
        }else{
            Core.perspectiveTransform(source,destination,H);
        }
        */
        Core.transform(source,destination,H);
    }

    @Override
    public int getLowerBound() {
        return LOWER_BOUND;
    }

    @Override
    public void setPointOverload(final PointOverloadEnum overload){
        this.overload = overload;
    }

    @Override
    public PointOverloadEnum getPointOverload() {
        return this.overload;
    }
}
