package com.ds4h.model.util.imageManager;

import ij.IJ;
import ij.process.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.Objects;

public class ImageProcessorMatConverter {

    private ImageProcessorMatConverter(){

    }

    private static Mat toMat(final ShortProcessor sp){
        IJ.log("[TO MAT - SHORTPROCESSOR] From ShortProcessor");
        final Mat matrix = new Mat(sp.getHeight(), sp.getWidth(), CvType.CV_16U);
        IJ.log("[TO MAT - SHORTPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, (short[]) sp.getPixels());
        IJ.log("[TO MAT - SHORTPROCESSOR] Matrix created");
        return  matrix;
    }

    private static Mat toMat(final ColorProcessor cp){
        IJ.log("[TO MAT - COLORPROCESSOR] From ColorProcessor " + ((int[])cp.getPixels()).length ) ;
        final Mat matrix = new Mat(cp.getHeight(), cp.getWidth(), CvType.CV_8UC3);
        final int[] pixels = (int[]) cp.getPixels();
        byte[] bData = new byte[cp.getWidth() * cp.getHeight() * 3];

        // convert int-encoded RGB values to byte array
        for (int i = 0; i < pixels.length; i++) {
            bData[i * 3] = (byte) ((pixels[i] >> 16) & 0xFF);	// red
            bData[i * 3 + 1] = (byte) ((pixels[i] >> 8) & 0xFF);	// grn
            bData[i * 3 + 2] = (byte) ((pixels[i]) & 0xFF);	// blu
        }
        IJ.log("[TO MAT - COLORPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, bData);
        IJ.log("[TO MAT - COLORPROCESSOR] Matrix created");
        return  matrix;
    }

    private static Mat toMat(final FloatProcessor fp){
        IJ.log("[TO MAT - FLOATPROCESSOR] From FloatProcessor");
        final Mat matrix = new Mat(fp.getHeight(), fp.getWidth(), CvType.CV_32FC1);
        IJ.log("[TO MAT - FLOATPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, (float[]) fp.getPixels());
        IJ.log("[TO MAT - FLOATPROCESSOR] Matrix created");
        return  matrix;
    }

    private static Mat toMat(final ByteProcessor bp){
        IJ.log("[TO MAT - BYTEPROCESSOR] From ByteProcessor");
        final Mat matrix = new Mat(bp.getHeight(), bp.getWidth(), CvType.CV_8U);
        IJ.log("[TO MAT - BYTEPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, (byte[]) bp.getPixels());
        IJ.log("[TO MAT - BYTEPROCESSOR] Matrix created");
        return  matrix;
    }

    /**
     * Convert the input "ImageProcessor" in to the corresponding Matrix.
     * @param ip the input processor, It represents the original Image.
     * @return the matrix with all the data of the image.
     * @throws IllegalArgumentException if the "ImageProcessor" is null.
     */
    public static Mat convert(final ImageProcessor ip) throws IllegalArgumentException{
        IJ.log("[IMAGE PROCESSOR CONVERTER] Ip: " + ip);
        if(Objects.nonNull(ip)){
            if(ip instanceof ColorProcessor){
                return ImageProcessorMatConverter.toMat((ColorProcessor) ip);
            }else if(ip instanceof ShortProcessor){
                return ImageProcessorMatConverter.toMat((ShortProcessor) ip);
            }else if(ip instanceof FloatProcessor){
                return ImageProcessorMatConverter.toMat((FloatProcessor) ip);
            }else if(ip instanceof ByteProcessor){
                return ImageProcessorMatConverter.toMat((ByteProcessor) ip);
            }
        }
        throw new IllegalArgumentException("The type of your image processor is not handled.");
    }

    /**
     * Convert the input "ImageProcessor" in to a gray scale matrix.
     * @param ip the input "ImageProcessor".
     * @return the gray scale matrix.
     * @throws IllegalArgumentException if the input is null.
     */
    public static Mat convertGray(final ImageProcessor ip) throws IllegalArgumentException {
        if(Objects.nonNull(ip)) {
            final Mat matrix = ImageProcessorMatConverter.convert(ip);
            Core.normalize(matrix, matrix, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
            IJ.log("[TO MAT] Converted: " + matrix);
            return matrix;
        }
        throw new IllegalArgumentException("The image processor is null. The conversion to gray can not be done.");
    }
}
