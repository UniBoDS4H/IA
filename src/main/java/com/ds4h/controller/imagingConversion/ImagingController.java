package com.ds4h.controller.imagingConversion;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.ImagingConversion;
import ij.ImagePlus;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

public class ImagingController {

    public ImagingController(){

    }

    public static ImagePlus fromGray2Rgb(final AlignedImage image){
        final Mat rgbMatrix = ImagingConversion.fromGray2Rgb(image.getMat());
        return new ImagePlus(image.getAlignedImage().getTitle(), HighGui.toBufferedImage(rgbMatrix));
    }
}
