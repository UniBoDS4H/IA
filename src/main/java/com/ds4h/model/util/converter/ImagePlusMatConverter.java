package com.ds4h.model.util.converter;

import ij.IJ;
import ij.ImagePlus;
import ij.process.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.Objects;

public class ImagePlusMatConverter {

    private ImagePlusMatConverter(){

    }

    /**
     *
     * @param sp
     * @return
     */
    private static Mat toMat(final ShortProcessor sp){
        IJ.log("[TO MAT - SHORTPROCESSOR] From ShortProcessor");
        final Mat matrix = new Mat(sp.getHeight(), sp.getWidth(), CvType.CV_16U);
        IJ.log("[TO MAT - SHORTPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, (short[]) sp.getPixels());
        IJ.log("[TO MAT - SHORTPROCESSOR] Matrix created");
        return  matrix;
    }

    /**
     *
     * @param cp
     * @return
     */
    private static Mat toMat(final ColorProcessor cp){
        IJ.log("[TO MAT - COLORPROCESSOR] From ColorProcessor " + ((int[])cp.getPixels()).length ) ;
        final Mat matrix = new Mat(cp.getHeight(), cp.getWidth(), CvType.CV_8UC3);
        final int[] pixels = (int[]) cp.getPixels();
        byte[] bData = new byte[cp.getWidth() * cp.getHeight() * 3];

        // convert int-encoded RGB values to byte array
        for (int i = 0; i < pixels.length; i++) {
            bData[i * 3 + 0] = (byte) ((pixels[i] >> 16) & 0xFF);	// red
            bData[i * 3 + 1] = (byte) ((pixels[i] >> 8) & 0xFF);	// grn
            bData[i * 3 + 2] = (byte) ((pixels[i]) & 0xFF);	// blu
        }
        IJ.log("[TO MAT - COLORPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, bData);
        IJ.log("[TO MAT - COLORPROCESSOR] Matrix created");
        return  matrix;
    }

    /**
     *
     * @param fp
     * @return
     */
    private static Mat toMat(final FloatProcessor fp){
        IJ.log("[TO MAT - FLOATPROCESSOR] From FloatProcessor");
        final Mat matrix = new Mat(fp.getHeight(), fp.getWidth(), CvType.CV_32FC1);
        IJ.log("[TO MAT - FLOATPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, (float[]) fp.getPixels());
        IJ.log("[TO MAT - FLOATPROCESSOR] Matrix created");
        return  matrix;
    }

    /**
     *
     * @param bp
     * @return
     */
    private static Mat toMat(final ByteProcessor bp){
        IJ.log("[TO MAT - BYTEPROCESSOR] From ByteProcessor");
        final Mat matrix = new Mat(bp.getHeight(), bp.getWidth(), CvType.CV_8UC1);
        IJ.log("[TO MAT - BYTEPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, (byte[]) bp.getPixels());
        IJ.log("[TO MAT - BYTEPROCESSOR] Matrix created");
        return  matrix;
    }

    /**
     *
     * @param ip
     * @return
     */
    public static Mat convert(final ImageProcessor ip){
        if(!Objects.isNull(ip)){
            if(ip instanceof ColorProcessor){
                return ImagePlusMatConverter.toMat((ColorProcessor) ip);
            }else if(ip instanceof ShortProcessor){
                return ImagePlusMatConverter.toMat((ShortProcessor) ip);
            }else if(ip instanceof FloatProcessor){
                return ImagePlusMatConverter.toMat((FloatProcessor) ip);
            }else if(ip instanceof ByteProcessor){
                return ImagePlusMatConverter.toMat((ByteProcessor) ip);
            }
        }
        throw new IllegalArgumentException("This ImageProcessor is not handled by this Software. Please contact Us, in order to " +
                "improve this Software.");
    }

    /**
     *
     * @param ip
     * @return
     */
    public static Mat convertGray(final ImageProcessor ip){
        final Mat matrix = ImagePlusMatConverter.convert(ip);
        if (!(ip instanceof ByteProcessor)) {
            IJ.log("[CONVERT GRAY] IS NOT A BYTEPROCESSOR");
            //0.00390625
            Core.multiply(matrix, new Scalar(1 / (double) 256), matrix);
        }
        matrix.convertTo(matrix, CvType.CV_8UC1);
        IJ.log("[TO MAT] Converted: " + matrix);
        return matrix;
    }
}
