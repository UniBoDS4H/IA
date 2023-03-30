package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.imagePoints.ImagePoints;
import ij.IJ;
import ij.process.ImageProcessor;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public abstract class PointDetector {
    private double factor = 0;
    public PointDetector(){

    }

    /**
     *
     * @param targetImage
     * @param imagePoint
     * @param scalingFactor
     */
    public abstract void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor);

    /**
     *
     * @param factor
     */
    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = factor;
        }
    }

    public double getFactor(){
        return this.factor;
    }

    /**
     *
     * @param matrix
     * @param levels
     * @return
     */
    protected Mat createPyramid(final Mat matrix, final int levels){
        Size lastSize = matrix.size();
        IJ.log("[POINT DETECTOR] Resize original matrix by: " + levels + " times");
        for(int i = 1; i < levels; i++){
            Imgproc.resize(matrix, matrix, new Size(lastSize.width / 2,
                    lastSize.height /2),
                    Imgproc.INTER_LINEAR);
            lastSize = matrix.size();
        }
        return matrix;
    }

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
