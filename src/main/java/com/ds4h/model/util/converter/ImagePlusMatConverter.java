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

    public static Mat toMat(final ShortProcessor sp){
        IJ.log("[TO MAT] From Short Processor");
        final Mat matrix = new Mat(sp.getHeight(), sp.getWidth(), CvType.CV_16U);
        IJ.log("[TO MAT] Matrix: " + matrix);
        matrix.put(0,0, (short[]) sp.getPixels());
        IJ.log("[TO MAT] Matrix created");
        return  matrix;
    }

    public static Mat convert(final ImageProcessor ip){
        if(!Objects.isNull(ip)){
            if(ip instanceof ColorProcessor){

            }else if(ip instanceof ShortProcessor){
                return ImagePlusMatConverter.toMat((ShortProcessor) ip);
            }else if(ip instanceof FloatProcessor){

            }else if(ip instanceof ByteProcessor){

            }
        }
        return null;
    }
    public static Mat convertGray(final ImageProcessor ip){
        final Mat matrix = ImagePlusMatConverter.convert(ip);
        Core.multiply(matrix, new Scalar(1/(double)256), matrix);
        matrix.convertTo(matrix, CvType.CV_8UC3);
        IJ.log("[TO MAT] Converted: " + matrix);
        return matrix;
    }
}
