package com.ds4h.model.util.imageManager;

import ij.IJ;
import ij.process.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Objects;

/**
 *
 */
public class MatImageProcessorConverter {

    /**
     *
     */
    private MatImageProcessorConverter(){

    }

    /**
     *
     * @param matrix a
     * @param width b
     * @param height c
     * @return d
     */
    private static ColorProcessor makeColorProcessor(Mat matrix, final int width, final int height){
        IJ.log("[MAKE COLORPROCESSOR] Creating the ColorProcessor using the LUT");
        final ColorProcessor cp = new ColorProcessor(width, height);
        byte[] pixels = new byte[width * height * matrix.channels()];
        matrix.get(0,0, pixels);
        matrix.release();
        matrix = null;
        int[] iData = (int[]) cp.getPixels();
        for (int i = 0; i < width * height; i++) {
            int red = pixels[i * 3] & 0xff;
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
     * @param matrix a
     * @param width b
     * @param height c
     * @param lut d
     * @param min e
     * @param max f
     * @return g
     */
    private static ImageProcessor makeShortProcessor(Mat matrix, final int width, final int height, final LUT lut, final double min, final double max){
        IJ.log("[MAKE SHORTPROCESSOR] Creating the ShortProcessor using the LUT");
        // final Mat newMatrix = new Mat(matrix.size(), CvType.CV_16U);
        IJ.log("[MAKE SHORTPROCESSOR] From: " + matrix);
        final ImageProcessor shortProcessor = new ShortProcessor(width, height);
        if(matrix.channels() > 1) {
            //We use the LUT table for the colors
            Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY);
        }
        IJ.log("[MAKE SHORTPROCESSOR] Matrix Type: " + matrix);
        matrix.get(0,0, (short[]) shortProcessor.getPixels());
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
     * @param matrix a
     * @param width b
     * @param height c
     * @return d
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
     * @param matrix a
     * @param width b
     * @param height c
     * @return d
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
        System.gc();
        IJ.log("[MAKE FLOATPROCESSOR] Finish creation FloatProcessor");
        return floatProcessor;
    }

    /**
     *
     * @param matrix a
     * @param width b
     * @param height c
     * @return d
     */
    private static BinaryProcessor makeBinaryProcessor(final Mat matrix, final int width, final int height){
        IJ.log("[MAKE BINARYPROCESSOR] Creating BinaryProcessor");
        final BinaryProcessor binaryProcessor = new BinaryProcessor(MatImageProcessorConverter.makeByteProcessor(matrix, width, height));
        IJ.log("[MAKE FLOATPROCESSOR] Finish creation FloatProcessor");
        return binaryProcessor;
    }

    /**
     *
     * @param matrix a
     * @param ip b
     * @return c
     */
    public static ImageProcessor convert(final Mat matrix, final ImageProcessor ip){
        if(Objects.nonNull(matrix) && Objects.nonNull(ip) && !matrix.empty()){
            if(ip instanceof ColorProcessor){
                return MatImageProcessorConverter.makeColorProcessor(matrix, matrix.cols(), matrix.rows());
            }else if(ip instanceof ShortProcessor){
                return MatImageProcessorConverter.makeShortProcessor(matrix, matrix.cols(), matrix.rows(),
                                ip.getLut(),
                                ip.getMin(),
                                ip.getMax());
            }else if(ip instanceof FloatProcessor){
                return MatImageProcessorConverter.makeFloatProcessor(matrix, matrix.cols(), matrix.rows());
            }else if(ip instanceof ByteProcessor){
                return MatImageProcessorConverter.makeByteProcessor(matrix, matrix.cols(), matrix.rows());
            }else{
                return MatImageProcessorConverter.makeBinaryProcessor(matrix, matrix.cols(), matrix.rows());
            }
        }else{
            throw new IllegalArgumentException("One of the argument is empty. Please check again the values");
        }
    }
}
