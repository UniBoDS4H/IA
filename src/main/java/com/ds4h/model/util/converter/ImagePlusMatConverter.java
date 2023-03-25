package com.ds4h.model.util.converter;

import ij.IJ;
import ij.ImagePlus;
import ij.process.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Objects;

public class ImagePlusMatConverter {

    private ImagePlusMatConverter(){

    }

    /**
     *
     * @param sp
     * @return
     */
    public static Mat toMat(final ShortProcessor sp){
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
    public static Mat toMat(final ColorProcessor cp){
        IJ.log("[TO MAT - COLORPROCESSOR] From ColorProcessor");
        final Mat matrix = new Mat(cp.getHeight(), cp.getWidth(), CvType.CV_8UC3);
        IJ.log("[TO MAT - COLORPROCESSOR] Matrix: " + matrix);
        matrix.put(0,0, (byte[]) cp.getPixels());
        IJ.log("[TO MAT - COLORPROCESSOR] Matrix created");
        return  matrix;
    }

    /**
     *
     * @param fp
     * @return
     */
    public static Mat toMat(final FloatProcessor fp){
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
    public static Mat toMat(final ByteProcessor bp){
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
            Core.multiply(matrix, new Scalar(1 / (double) 256), matrix);
        }
        matrix.convertTo(matrix, CvType.CV_8UC3);
        IJ.log("[TO MAT] Converted: " + matrix);
        return matrix;
    }
}
