package com.ds4h.model.util;


import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import org.opencv.core.Mat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ImagingConversion {
    private ImagingConversion(){}

    public static Optional<ImagePlus> fromMatToImagePlus(final Mat matrix, final String fileName, final String separator){
        Optional<ImagePlus> output = Optional.empty();
        if(!matrix.empty() && !fileName.isEmpty() && !separator.isEmpty()){
            final byte[] data = new byte[matrix.rows() * matrix.cols() * (int)(matrix.elemSize())];
            matrix.get(0, 0, data);
            final String imgFinalName = new NameBuilder().parseName(fileName).splitBy(separator).getFinalName();
            final ImagePlus imp = new ImagePlus(imgFinalName, new ByteProcessor(matrix.cols(), matrix.rows(), data));
            output = Optional.of(imp);
        }
        return output;
    }

    public static List<ImagePlus> fromPathToImagePlus(final List<String> paths){
        List<ImagePlus> images = new LinkedList<>();
        for(final String path : paths){
            try {
                images.add(IJ.openImage(path));
            }catch (Exception e){
                IJ.showMessage("An error occurred with this file : " + path + ". Are you sure that is correct ?");
            }
        }
        return images;
    }
}
