package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.imagePoints.ImagePoints;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public abstract class PointDetector {

    private double factor = 0;

    public PointDetector(){

    }
    public abstract void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint, int scalingFactor);


    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = factor;
        }
    }

    public double getFactor(){
        return this.factor;
    }

    protected ImagePlus createPyramid(final ImagePoints image, final int levels){
        //int levels = 4; // Number of levels in the pyramid
        final String imageTitle  = image.getTitle();
        ImagePlus[] pyramid = new ImagePlus[levels]; // Array to store the pyramid

        pyramid[0] = image; // Store the original image as the first level

        for (int i = 1; i < levels; i++) {
            ImageProcessor ipDownsampled = pyramid[i - 1].getProcessor().resize(pyramid[i - 1].getWidth() / 2, pyramid[i - 1].getHeight() / 2); // Downsample the previous level by a factor of 2
            pyramid[i] = new ImagePlus(i+imageTitle, ipDownsampled); // Store the downsampled image as the current level
        }
        final ImagePlus scaledImage = new ImagePlus(imageTitle, pyramid[levels-1].getProcessor());
        System.gc();
        pyramid = null;
        return scaledImage;
    }
}
