package com.ds4h.model.util;

import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.util.logger.Logger;
import com.ds4h.model.util.logger.LoggerFactory;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import org.jetbrains.annotations.NotNull;

import java.awt.image.ColorModel;
import java.util.List;

public class ImageStackCreator {
    private final static Logger myLogger = LoggerFactory.getImageJLogger(ImageStackCreator.class);
    private ImageStackCreator() {}

    @NotNull
    public static ImagePlus createImageStack(@NotNull final List<AlignedImage> alignedImageList) {
        final ImagePlus alignedImage = alignedImageList.get(0).getAlignedImage();
        final ImageStack stack = new ImageStack(alignedImage.getWidth(), alignedImage.getHeight(), ColorModel.getRGBdefault());
        for (final AlignedImage image : alignedImageList) {
            myLogger.log("Current image stack: " + image.getName());
            stack.addSlice(image.getName(), image.getProcessor());
        }
        try {
            return new ImagePlus("AlignedStack", stack);
        }catch (final Exception e){
            myLogger.logError("Something went wrong with the creation of the stack: " + e.getMessage());
            throw new RuntimeException("Something went wrong with the creation of the stack.\n" +
                    "Error: " + e.getMessage());
        }
    }
}
