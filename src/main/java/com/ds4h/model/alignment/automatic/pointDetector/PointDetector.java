package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.saveProject.SaveImages;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public abstract class PointDetector {

    private double factor = 0;

    public PointDetector(){

    }
    public abstract void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint);

    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = factor;
        }
    }

    public double getFactor(){
        return this.factor;
    }

    protected Mat createPyramid(final ImagePoints image, final int levels){
        //int levels = 4; // Number of levels in the pyramid
        ImagePlus[] pyramid = new ImagePlus[levels]; // Array to store the pyramid

        pyramid[0] = image; // Store the original image as the first level

        for (int i = 1; i < levels; i++) {
            ImageProcessor ipDownsampled = pyramid[i - 1].getProcessor().resize(pyramid[i - 1].getWidth() / 2, pyramid[i - 1].getHeight() / 2); // Downsample the previous level by a factor of 2
            pyramid[i] = new ImagePlus("Rescaled" + i, ipDownsampled); // Store the downsampled image as the current level
        }
        //TODO: RESTORE THE SAVING OF THE IMAGES, FARE IN MODO CHE MI TORNI IL PATH COMPLETO.
        final String path = SaveImages.saveTMPImage(pyramid[levels-1]) + "/" + "Rescaled" + String.valueOf(levels-1) + ".tif";
        System.out.println(path);
        final ImagePoints scaledImage = new ImagePoints(path);
        //Save the last images
        //Read its matrix
        scaledImage.show();
        return scaledImage.getMatImage();
    }
}
