package com.ds4h.model.util;


import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ImagingConversion {
    private ImagingConversion(){}

    public static Optional<ImagePlus> fromMatToImagePlus(final Mat matrix, final String fileName, final String separator){
        try {
            if (!matrix.empty() && !fileName.isEmpty() && !separator.isEmpty()) {
                final String imgFinalName = new NameBuilder().parseName(fileName).splitBy(separator).getFinalName();
                final ImagePlus imp = new ImagePlus("PROVA", HighGui.toBufferedImage(matrix));
                return Optional.of(imp);
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
            if(Objects.nonNull(path)) {
                return !path.isEmpty() ? Optional.of(IJ.openImage(path)) : Optional.empty();
            }
        }catch(Exception e){
            IJ.showMessage("An error occurred with this file : " + path + ". Are you sure that is correct ?");
        }
        return Optional.empty();
    }
}
