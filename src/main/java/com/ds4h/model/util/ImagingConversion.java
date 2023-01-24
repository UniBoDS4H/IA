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
        try {
            if (!matrix.empty() && !fileName.isEmpty() && !separator.isEmpty()) {
                final byte[] data = new byte[matrix.rows() * matrix.cols() * (int) (matrix.elemSize())];
                matrix.get(0, 0, data);
                final String imgFinalName = new NameBuilder().parseName(fileName).splitBy(separator).getFinalName();
                final ImagePlus imp = new ImagePlus(imgFinalName, new ByteProcessor(matrix.cols(), matrix.rows(), data));
                final Optional<ImagePlus> output = Optional.of(imp);
                return output;
            }
        }catch (Exception e){
            IJ.showMessage(e.getMessage());
        }
        return Optional.empty();
    }

    public static List<ImagePlus> fromPathToImagePlus(final List<String> paths){
        List<ImagePlus> images = new LinkedList<>();
        for(final String path : paths){
            try {
                if(!path.isEmpty()){
                    images.add(IJ.openImage(path));
                }
            }catch (Exception e){
                IJ.showMessage("An error occurred with this file : " + path + ". Are you sure that is correct ?");
            }
        }
        return images;
    }

    public static Optional<ImagePlus> fromSinglePathToImagePlus(final String path){
        try {
            final Optional<ImagePlus> output = !path.isEmpty() ? Optional.of(IJ.openImage(path)) : Optional.empty();
            return output;
        }catch(Exception e){
            IJ.showMessage("An error occurred with this file : " + path + ". Are you sure that is correct ?");
        }
        return Optional.empty();
    }
}
