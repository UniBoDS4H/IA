package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;
import ij.process.ImageProcessor;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 *
 */
public abstract class PointDetector {
    private double factor = 0;
    final MatCache matCache;

    public PointDetector(){
        this.matCache = new MatCache();
    }

    public MatCache getMatCache(){
        return this.matCache;
    }

    public void clearCache(){
        this.matCache.releaseMatrix();
    }

    /**
     * @param targetImage
     * @param imagePoint
     * @param scalingFactor
     */
    public abstract void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, final int scalingFactor);

    /**
     *
     * @param factor
     */
    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = factor;
        }
    }

    /**
     *
     * @return
     */
    public double getFactor(){
        return this.factor;
    }

    /**
     *
     * @param matrix
     * @param levels
     * @return
     */
    protected Mat createPyramid(Mat matrix, final int levels){
        Size lastSize = matrix.size();
        IJ.log("[POINT DETECTOR] Resize original matrix by: " + levels + " times.");
        for(int i = 1; i < levels; i++){
            Imgproc.resize(matrix, matrix,
                    new Size(lastSize.width / 2, lastSize.height /2),
                    Imgproc.INTER_LINEAR);
            lastSize = matrix.size();
        }
        IJ.log("[POINT DETECTOR] Matrix:" + matrix + ".");
        IJ.log("[POINT DETECTOR] Resize done.");
        return this.improveMatrix(matrix);
    }

    private Mat improveMatrix(final Mat matrix){
        final double mean = Core.mean(matrix).val[0];
        final double percentage = (mean/255.0 * 100.0);
        IJ.log("[MEAN] Percentage: " + percentage);
        if(percentage >= 60.0){
            final int ksize = 3;
            //final Mat inverted = new Mat();
            //Core.bitwise_not(matrix, inverted);
            //Core.bitwise_and(matrix, inverted, matrix);

            //Reduce the Noise
            final Mat filteredM = new Mat();
            Imgproc.bilateralFilter(matrix, filteredM, 5, mean, mean);
            IJ.log("[POINT DETECTOR > IMPROVE MATRIX] Done filtering.");
            final Mat destination = new Mat();
            Imgproc.Laplacian(filteredM, destination, CvType.CV_64F, ksize, 1, 0);
            Core.convertScaleAbs(destination, destination);
            Core.bitwise_and(matrix, destination, matrix);

            //new ImagePlus("ciao", MatImageProcessorConverter.convert(matrix, "ciao", new ByteProcessor(0, 0))).show();

            return matrix;

        }
        return matrix;
    }
        /*


            final Mat sobelx = new Mat();
            final Mat sobely = new Mat();
            Imgproc.Sobel(matrix, sobelx, CvType.CV_32F, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
            Imgproc.Sobel(matrix, sobely, CvType.CV_32F, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT);
            Core.magnitude(sobelx, sobely, matrix);
            Core.normalize(matrix, matrix, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);


        final Mat sharx = new Mat();
        final Mat shary = new Mat();
        Imgproc.Scharr(matrix, sharx, CvType.CV_64F, 1,0);
        Imgproc.Scharr(matrix, shary, CvType.CV_64F, 0, 1);
        Core.magnitude(sharx, shary, matrix);
        Core.normalize(matrix, matrix, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);



        Mat kerner = Imgproc.getGaussianKernel(5, 2);
        Mat F_b = new Mat();
        Core.multiply(kerner.reshape(1, kerner.rows() * kerner.cols()), kerner, F_b);
        Mat F_id = Mat.zeros(kerner.size(), CvType.CV_32F);
        F_id.put(F_id.rows()/2, F_id.cols()/2, 1);
        Core.divide(F_b, Core.sumElems(F_b), F_b);
        final Mat d = new Mat();
        Imgproc.filter2D(matrix, d, -1, F_b);
        new ImagePlus("ciao", MatImageProcessorConverter.convert(matrix, "ciao", new ByteProcessor(0,0)))
                .show();
         */
    /**
     *
     * @param image
     * @param levels
     * @return
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
