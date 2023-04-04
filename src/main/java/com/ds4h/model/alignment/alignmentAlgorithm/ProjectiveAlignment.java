package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.converter.MatImageProcessorConverter;
import ij.process.ImageProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

public class ProjectiveAlignment implements AlignmentAlgorithm{
    public static int LOWER_BOUND = 4;
    private PointOverloadEnum overload;

    public ProjectiveAlignment(){
        this.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
    }
    @Override
    public AlignedImage align(ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException {
        if(targetImage.getPoints().length >= LOWER_BOUND && imageToShift.getPoints().length >= LOWER_BOUND){
            final Mat imageToShiftMat = imageToShift.getMatImage();
            final Mat alignedImage = new Mat();
            final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
            Imgproc.warpPerspective(imageToShiftMat, alignedImage, transformationMatrix, targetImage.getMatImage().size());
            return new AlignedImage(transformationMatrix, MatImageProcessorConverter.convert(alignedImage,
                    imageToShift.getName(), ip), imageToShift.getName());
        }else{
            throw new IllegalArgumentException("For the Projective alignment the points must be at least: 4");
        }
    }

    @Override
    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints) {
        return Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
    }

    @Override
    public void transform(Mat source, Mat destination, Mat H) {
        Core.perspectiveTransform(source,destination,H);
    }

    @Override
    public int getLowerBound() {
        return LOWER_BOUND;
    }

    @Override
    public void setPointOverload(PointOverloadEnum overload){
        this.overload = overload;
    }

    @Override
    public PointOverloadEnum getPointOverload() {
        return this.overload;
    }
}
