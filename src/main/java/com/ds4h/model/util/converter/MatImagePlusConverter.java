package com.ds4h.model.util.converter;

import com.ds4h.model.util.MemoryController;
import ij.IJ;
import ij.ImagePlus;
import ij.process.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MatImagePlusConverter {

    private MatImagePlusConverter(){

    }

    /**
     *
     * @param matrix
     * @param width
     * @param height
     * @return
     */
    private static ColorProcessor makeColorProcessor(Mat matrix, final int width, final int height){
        IJ.log("[MAKE COLORPROCESSOR] Creating the ColorProcessor using the LUT");
        final ColorProcessor cp = new ColorProcessor(width, height);
        byte[] pixels = new byte[width * height * matrix.channels()];
        matrix.get(0,0, pixels);
        matrix.release();
        matrix = null;
        int[] iData = (int[]) cp.getPixels();
        /*
        if(matrix.channels() != 3){
            matrix.convertTo(matrix, CvType.CV_8UC3);
        }
         */
        for (int i = 0; i < width * height; i++) {
            int red = pixels[i * 3 + 0] & 0xff;
            int grn = pixels[i * 3 + 1] & 0xff;
            int blu = pixels[i * 3 + 2] & 0xff;
            iData[i] = (red << 16) | (grn << 8) | blu;
        }

        System.gc();
        IJ.log("[MAKE COLORPROCESSOR] The creation is done");
        return cp;
    }

    /**
     *
     * @param matrix
     * @param width
     * @param height
     * @param lut
     * @param min
     * @param max
     * @return
     */
    private static ImageProcessor makeShortProcessor(Mat matrix, final int width, final int height, final LUT lut, final double min, final double max){
        IJ.log("[MAKE SHORTPROCESSOR] Creating the ShortProcessor using the LUT");
        // final Mat newMatrix = new Mat(matrix.size(), CvType.CV_16U);
        IJ.log("[MAKE SHORTPROCESSOR] From: " + matrix);
        final ImageProcessor shortProcessor = new ShortProcessor(width, height);
        MemoryController.controllMemory();

        if(matrix.channels() > 1) {
            //We use the LUT table for the colors
            Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY);
        }

        /*
        This is useless because when I read the matrix it is already 16 bit with one channel. Need to be tested
        if(matrix.type() != CvType.CV_16U) {
            //Convert all the values from 8 bit to 16 bit
            matrix.convertTo(matrix, CvType.CV_16U);
            Core.multiply(matrix, new Scalar(256), matrix);
        }
        */

        IJ.log("[MAKE SHORTPROCESSOR] Matrix Type: " + matrix);
        //matrix.release(); // release the old matrix
        matrix.get(0,0, (short[]) shortProcessor.getPixels()); // get all the values
        //shortProcessor.setPixels(pixels); // set the pixels
        matrix.release();
        matrix = null;
        shortProcessor.setMinAndMax(min, max);
        IJ.log("[MAKE SHORTPROCESSOR] End of creation ShortProcessor");
        shortProcessor.setLut(lut);
        System.gc();
        return shortProcessor;
    }

    /**
     *
     * @param matrix
     * @param width
     * @param height
     * @return
     */
    private static ByteProcessor makeByteProcessor(Mat matrix, final int width, final int height){
        IJ.log("[MAKE BYTEPROCESSOR] Creating ByteProcessor");
        final ByteProcessor ip = new ByteProcessor(width, height);
        if(matrix.channels() > 1) {
            Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY);
        }
        IJ.log("[MAKE BYTEPROCESSOR] Matrix: " + matrix);
        matrix.get(0,0, (byte[])ip.getPixels());
        matrix.release();
        matrix = null;
        System.gc();
        IJ.log("[MAKE BYTEPROCESSOR] Finish creation ByteProcessor");
        return ip;
    }

    /**
     *
     * @param matrix
     * @param width
     * @param height
     * @return
     */
    private static FloatProcessor makeFloatProcessor(Mat matrix, final int width, final int height){
        IJ.log("[MAKE FLOATPROCESSOR] Creating FloatProcessor");
        //final float[] pixels = new float[width*height];
        final FloatProcessor floatProcessor = new FloatProcessor(width, height);
        if(matrix.channels() > 1) {
            Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY);
        }
        matrix.convertTo(matrix, CvType.CV_32FC1);
        matrix.get(0,0, (float[])floatProcessor.getPixels());
        matrix.release();
        matrix = null;
        IJ.log("[MAKE FLOATPROCESSOR] Finish creation FloatProcessor");
        return floatProcessor;
    }

    /**
     *
     * @param matrix
     * @param width
     * @param height
     * @return
     */
    private static BinaryProcessor makeBinaryProcessor(final Mat matrix, final int width, final int height){
        IJ.log("[MAKE BINARYPROCESSOR] Creating BinaryProcessor");
        final BinaryProcessor binaryProcessor = new BinaryProcessor(MatImagePlusConverter.makeByteProcessor(matrix, width, height));
        IJ.log("[MAKE FLOATPROCESSOR] Finish creation FloatProcessor");
        return binaryProcessor;
    }

    /**
     *
     * @param matrix
     * @param fileName
     * @param ip
     * @return
     */
    public static ImagePlus convert(final Mat matrix, final String fileName, final ImageProcessor ip){
        if(!matrix.empty() && !fileName.isEmpty()){
            if(ip instanceof ColorProcessor){
                return new ImagePlus(fileName,
                        MatImagePlusConverter.makeColorProcessor(matrix, matrix.cols(), matrix.rows()));
            }else if(ip instanceof ShortProcessor){
                return new ImagePlus(fileName,
                        MatImagePlusConverter.makeShortProcessor(matrix, matrix.cols(), matrix.rows(),
                                ip.getLut(),
                                ip.getMin(),
                                ip.getMax()));
            }else if(ip instanceof FloatProcessor){
                return new ImagePlus(fileName,
                        MatImagePlusConverter.makeFloatProcessor(matrix, matrix.cols(), matrix.rows()));
            }else if(ip instanceof ByteProcessor){
                return new ImagePlus(fileName,
                        MatImagePlusConverter.makeByteProcessor(matrix, matrix.cols(), matrix.rows()));
            }else{
                return new ImagePlus(fileName,
                        MatImagePlusConverter.makeBinaryProcessor(matrix, matrix.cols(), matrix.rows()));
            }
        }else{
            throw new IllegalArgumentException("One of the argument is empty. Please check again the values");
        }
    }

}
