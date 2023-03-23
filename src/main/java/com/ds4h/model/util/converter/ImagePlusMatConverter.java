package com.ds4h.model.util.converter;

import ij.process.*;
import org.opencv.core.Mat;

import java.util.Objects;

public class ImagePlusMatConverter {

    private ImagePlusMatConverter(){

    }

    public static Mat convert(final ImageProcessor ip){
        if(!Objects.isNull(ip)){
            if(ip instanceof ColorProcessor){

            }else if(ip instanceof ShortProcessor){

            }else if(ip instanceof FloatProcessor){

            }else if(ip instanceof ByteProcessor){

            }
        }
        return null;
    }
}
