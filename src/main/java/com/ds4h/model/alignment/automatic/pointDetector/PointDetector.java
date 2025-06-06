package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.image.imagePoints.ImagePoints;
import ij.IJ;
import ij.process.ImageProcessor;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 *
 */
public abstract class PointDetector {
    public static final int LOWER_BOUND = 1;
    public static final int UPPER_BOUND = 8;
    public static final int DEFAULT = 4;
    private double factor = 0;
    private int scalingFactor = 1;
    final MatCache matCache;

    /**
     * Constructor for the PointDetector object.
     */
    public PointDetector(){
        this.matCache = new MatCache();
    }

    /**
     * Returns the MatCache object.
     * @return
     */
    public MatCache getMatCache(){
        return this.matCache;
    }

    /**
     * Clear the cache from the Detector and MatOfKeyPoint.
     */
    public void clearCache(){
        this.matCache.releaseMatrix();
    }

    /**
     * Detect the key points inside the two images.
     * @param targetImage the target image selected from the input, this image will be cached during the entire process of alignment.
     * @param imagePoint the other image where the algorithm have to detect points.
     */
    public abstract void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint);

    /**
     * Set the threshold factor, in order to get more "dirty" points in the detection.
     * @param factor how many "dirty" points we should get.
     */
    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = factor;
        }
    }

    /**
     * Set the scaling factor for the images.
     * @param scaling how many times the images must be scaled.
     */
    public void setScalingFactor(final int scaling){
        if(scaling >= PointDetector.LOWER_BOUND && scaling <= PointDetector.UPPER_BOUND){
            this.scalingFactor = scaling;
        }
    }

    /**
     * Returns the scaling factor.
     * @return the scaling factor.
     */
    public int getScalingFactor(){
        return this.scalingFactor;
    }

    /**
     * Returns the threshold factor.
     * @return the threshold factor.
     */
    public double getFactor(){
        return this.factor;
    }

    /**
     * Creates the Pyramid from the input matrix. The input matrix is resized by 2^(levels) times. This will be used when the images
     * are too big for the detection, so we should scale the image, detect the all the possible points, and then rescale all the
     * detected points in to the original image.
     * @param matrix the image to be resized.
     * @param levels the pyramids' depth.
     * @return the new matrix, resized by 2^(levels) times.
     */
    protected Mat createPyramid(final Mat matrix, final int levels){
        Size lastSize = matrix.size();
        IJ.log("[POINT DETECTOR] Resize original matrix by: " + levels + " times.");
        for(int i = 1; i < levels; i++) {
            Imgproc.resize(matrix, matrix,
                    new Size(lastSize.width / 2, lastSize.height / 2),
                    Imgproc.INTER_AREA);
            lastSize = matrix.size();
        }
        IJ.log("[POINT DETECTOR] Matrix:" + matrix + ".");
        IJ.log("[POINT DETECTOR] Resize done.");
        return matrix;
    }

    /**
     * This method can be used in order to improve the input matrix with the "Edge Detection".
     * @param matrix the input matrix
     * @return the improved matrix.
     */
    protected Mat improveMatrix(final Mat matrix){
        final double mean = Core.mean(matrix).val[0];
        final double percentage = (mean/255.0 * 100.0);
        IJ.log("[MEAN] Percentage: " + percentage);
        final int ksize = 3;

        if(percentage >= 60.0){
            //Reduce the Noise
            Mat filteredM = new Mat();
            Mat destination = new Mat();
            Imgproc.bilateralFilter(matrix, filteredM, 5, mean, mean);
            //Edge detection
            Imgproc.Laplacian(filteredM, destination, CvType.CV_64F, ksize, 1, 0);
            Core.convertScaleAbs(destination, destination);
            //Use the border as mask
            Core.bitwise_and(matrix, destination, matrix);
            filteredM.release();
            destination.release();
            filteredM = null;
            destination = null;
            System.gc();
            return matrix;
        }else{
            final Mat blur = new Mat();
            Imgproc.medianBlur(matrix, blur, ksize);
            final Mat sobelx = new Mat();
            final Mat sobely = new Mat();
            Imgproc.Sobel(blur, sobelx, CvType.CV_64F, 1, 0, ksize, 1, 0, Core.BORDER_DEFAULT);
            Imgproc.Sobel(blur, sobely, CvType.CV_64F, 0, 1, ksize, 1, 0, Core.BORDER_DEFAULT);
            Core.magnitude(sobelx, sobely, blur);
            Core.normalize(blur, blur, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
            matrix.release();
            System.gc();
            return blur;
        }
    }

    /**
     * Creates the pyramid for the input ImagePoints. We rescale the ImageProcessor in order to get a smaller image.
     * The scaled image is used for the detection but the detected points are then rescaled to the original matrix.
     * @param image the input image to rescale.
     * @param levels how many levels we have to create. We rescale about 2^(levels) times.
     * @return the scaled image.
     */
    protected ImageProcessor createPyramid(final ImagePoints image, final int levels){
        final ImageProcessor[] pyramid = new ImageProcessor[levels];
        pyramid[0] = image.getProcessor();
        IJ.log("[POINT DETECTOR] Resize original ImageProcessor by: " + levels + " times");
        for (int i = 1; i < levels; i++) {
            ImageProcessor ipDownsampled = pyramid[i - 1].resize(
                    pyramid[i - 1].getWidth() / 2,
                    pyramid[i - 1].getHeight() / 2);
            pyramid[i] = ipDownsampled;
        }
        return pyramid[levels-1];
    }
}
