package com.ds4h.model.util;

import com.ds4h.model.image.alignedImage.AlignedImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import org.jetbrains.annotations.NotNull;

import java.awt.image.ColorModel;
import java.util.List;

public class ImageStackCreator {
    private ImageStackCreator() {}

    @NotNull
    public static ImagePlus createImageStack(@NotNull final List<AlignedImage> alignedImageList) {
        final ImagePlus alignedImage = alignedImageList.get(0).getAlignedImage();
        final ImageStack stack = new ImageStack(alignedImage.getWidth(), alignedImage.getHeight(), ColorModel.getRGBdefault());
        for (final AlignedImage image : alignedImageList) {
            IJ.log("[STACK CREATION] Current image: " + image.getName());
            stack.addSlice(image.getName(), image.getProcessor());
        }
        try {
            return new ImagePlus("AlignedStack", stack);
        }catch (final Exception e){
            throw new RuntimeException("Something went wrong with the creation of the stack.\n" +
                    "Error: " + e.getMessage());
        }
    }
}
