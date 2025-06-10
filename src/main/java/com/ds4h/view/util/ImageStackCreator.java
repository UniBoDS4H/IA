package com.ds4h.view.util;

import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.alignedImage.AlignedImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.LUT;
import org.jetbrains.annotations.NotNull;

import java.awt.image.ColorModel;
import java.util.List;

public class ImageStackCreator {
    private ImageStackCreator() {}

    @NotNull
    public static ImagePlus createImageStack(@NotNull final List<AlignedImage> alignedImageList) {
        final ImagePlus alignedImage = alignedImageList.get(0).getAlignedImage();
        final ImageStack stack = new ImageStack(alignedImage.getWidth(), alignedImage.getHeight(), ColorModel.getRGBdefault());
        final LUT[] luts = new LUT[alignedImageList.size()];
        int index = 0;
        for (final AlignedImage image : alignedImageList) {
            luts[index] = image.getAlignedImage().getProcessor().getLut();
            IJ.log("[NAME] " + image.getName());
            stack.addSlice(image.getName(), image.getAlignedImage().getProcessor());
            index++;
        }
        try {
                /*final CompositeImage composite = new CompositeImage(new ImagePlus("AlignedStack", stack));
                composite.setLuts(luts);
                return composite;*/
            return new ImagePlus("AlignedStack", stack);
        }catch (final Exception e){
            throw new RuntimeException("Something went wrong with the creation of the stack.\n" +
                    "Error: " + e.getMessage());
        }
    }
}
