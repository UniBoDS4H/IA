package com.ds4h.model.util;

import ij.ImagePlus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ChangeImage {

    private ChangeImage(){

    }

    public static ImagePlus changeColor(final ImagePlus inputImage, final Color color){
        final BufferedImage img = inputImage.getBufferedImage();
        final BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < img.getWidth(); i++){
            for(int j = 0; j < img.getHeight(); j++){
                final Color c = new Color(img.getRGB(i,j));
                final int r = color.getRed()*c.getRed()/255 -40;
                final int g = color.getGreen()*c.getGreen()/255 -40;
                final int b = color.getBlue()*c.getBlue()/255 -40;
                final int a = c.getAlpha();
                final Color nc = new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0), a);
                image.setRGB(i, j, nc.getRGB());
            }
        }
        return new ImagePlus(inputImage.getTitle(), image);
    }

    public static ImagePlus changeContrast(final ImagePlus inputImage, final float scaleFactor){
        final BufferedImage bufferedImage = inputImage.getBufferedImage();
        float offset = -(scaleFactor * 128f) + 128f;
        final RescaleOp rescaleOp = new RescaleOp(scaleFactor, offset, null);
        BufferedImage adjustedImage = rescaleOp.filter(bufferedImage, null);
        return new ImagePlus(inputImage.getTitle(), adjustedImage);
    }

    public static ImagePlus invert(final ImagePlus imagePlus){
        final BufferedImage originalImage = imagePlus.getBufferedImage();

        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int pixel = originalImage.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                red = 255 - red;
                green = 255 - green;
                blue = 255 - blue;
                pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                originalImage.setRGB(x, y, pixel);
            }
        }
        return new ImagePlus(imagePlus.getTitle(), originalImage);
    }
}
