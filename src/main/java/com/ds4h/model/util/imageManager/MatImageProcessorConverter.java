package com.ds4h.model.util.imageManager;

import ij.IJ;
import ij.process.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Objects;

/**
 * With the using of this class we can create a new Image starting from a Mat object. The image that will be created It would be
 * as the same type of the original image (ShortProcessor, ByteProcessor, ...).
 */
public class MatImageProcessorConverter {

    private MatImageProcessorConverter(){

    }

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
        IJ.log("[MAKE COLORPROCESSOR] The creation is done");
        return cp;
    }

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
        return shortProcessor;
    }

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
        IJ.log("[MAKE BYTEPROCESSOR] Finish creation ByteProcessor");
        return ip;
    }

    private static FloatProcessor makeFloatProcessor(Mat matrix, final int width, final int height, final LUT lut){
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
        if(Objects.nonNull(lut)){
            floatProcessor.setLut(lut);
        }
        IJ.log("[MAKE FLOATPROCESSOR] Finish creation FloatProcessor");
        return floatProcessor;
    }

    private static BinaryProcessor makeBinaryProcessor(final Mat matrix, final int width, final int height){
        IJ.log("[MAKE BINARYPROCESSOR] Creating BinaryProcessor");
        final BinaryProcessor binaryProcessor = new BinaryProcessor(MatImageProcessorConverter.makeByteProcessor(matrix, width, height));
        IJ.log("[MAKE FLOATPROCESSOR] Finish creation FloatProcessor");
        return binaryProcessor;
    }

    /**
     * Convert the input matrix in a "ImageProcessor" of the same type of "ip".
     * @param matrix the input matrix, where all the values are stored.
     * @param ip the original ImageProcessor.
     * @return the final ImageProcessor with all the new data from the matrix.
     * @throws IllegalArgumentException if one of the parameters are null.
     */
    public static ImageProcessor convert(final Mat matrix, final ImageProcessor ip) throws IllegalArgumentException{
        if(Objects.nonNull(matrix) && Objects.nonNull(ip) && !matrix.empty()){
            if(ip instanceof ColorProcessor){
                return MatImageProcessorConverter.makeColorProcessor(matrix, matrix.cols(), matrix.rows());
            }else if(ip instanceof ShortProcessor){
                return MatImageProcessorConverter.makeShortProcessor(matrix, matrix.cols(), matrix.rows(),
                                ip.getLut(),
                                ip.getMin(),
                                ip.getMax());
            }else if(ip instanceof FloatProcessor){
                return MatImageProcessorConverter.makeFloatProcessor(matrix, matrix.cols(), matrix.rows(), ip.getLut());
            }else if(ip instanceof ByteProcessor){
                return MatImageProcessorConverter.makeByteProcessor(matrix, matrix.cols(), matrix.rows());
            }else{
                return MatImageProcessorConverter.makeBinaryProcessor(matrix, matrix.cols(), matrix.rows());
            }
        }else{
            throw new IllegalArgumentException("One of the argument is empty. Please check again the values.");
        }
    }
}
