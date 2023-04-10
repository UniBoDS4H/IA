package com.ds4h.model.alignment.alignmentAlgorithm;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.converter.MatImageProcessorConverter;
import ij.process.ImageProcessor;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

public class ProjectiveAlignment implements AlignmentAlgorithm{
    public static int LOWER_BOUND = 4;
    private PointOverloadEnum overload;

    /**
     *
     */
    public ProjectiveAlignment(){
        this.setPointOverload(PointOverloadEnum.FIRST_AVAILABLE);
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
    public AlignedImage align(ImagePoints targetImage, final ImagePoints imageToShift, final ImageProcessor ip) throws IllegalArgumentException {
        if(targetImage.getPoints().length >= LOWER_BOUND && imageToShift.getPoints().length >= LOWER_BOUND){
            final Mat imageToShiftMat = imageToShift.getMatImage();
            final Mat alignedImage = new Mat();
            final Mat transformationMatrix = this.getTransformationMatrix(imageToShift.getMatOfPoint(), targetImage.getMatOfPoint());
            Imgproc.warpPerspective(imageToShiftMat, alignedImage, transformationMatrix, targetImage.getMatImage().size());
            return new AlignedImage(transformationMatrix, MatImageProcessorConverter.convert(alignedImage, ip), imageToShift.getName());
        }else{
            throw new IllegalArgumentException("For the Projective alignment the points must be at least: 4");
        }
    }

    /**
     *
     * @param srcPoints a
     * @param dstPoints b
     * @return c
     */
    @Override
    public Mat getTransformationMatrix(final MatOfPoint2f srcPoints, final MatOfPoint2f dstPoints) {
        //return Imgproc.getPerspectiveTransform(srcPoints, dstPoints); THIS IS THE ORIGINAL!!
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

    /**
     *
     * @param source a
     * @param destination b
     * @param H c
     */
    @Override
    public void transform(final Mat source,final Mat destination, final Mat H) {
        Core.perspectiveTransform(source,destination,H);
    }

    /**
     *
     * @return a
     */
    @Override
    public int getLowerBound() {
        return LOWER_BOUND;
    }

    /**
     *
     * @param overload a
     */
    @Override
    public void setPointOverload(final PointOverloadEnum overload){
        this.overload = overload;
    }

    /**
     *
     * @return a
     */
    @Override
    public PointOverloadEnum getPointOverload() {
        return this.overload;
    }
}
