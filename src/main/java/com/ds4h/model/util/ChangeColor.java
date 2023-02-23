package com.ds4h.model.util;

import ij.ImagePlus;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChangeColor {

    private ChangeColor(){

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
}
