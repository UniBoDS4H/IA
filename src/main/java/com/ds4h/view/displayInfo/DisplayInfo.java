package com.ds4h.view.displayInfo;

import jdk.jfr.Percentage;

import java.awt.*;

public final class DisplayInfo {
    private DisplayInfo(){}

    public static Dimension getDisplaySize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    public static Dimension getDisplaySize(int percentage){
        if(percentage >100 || percentage < 0){
            throw new IllegalArgumentException("The given percentage was not between 0 and 100");
        }
        final Dimension oldDim = DisplayInfo.getDisplaySize();
        final Dimension newDim = new Dimension((int)(percentage*oldDim.getWidth()/100),(int)(percentage*oldDim.getHeight()/100));
        return newDim;
    }
    public static Dimension getScaledImageDimension(Dimension imgDimension, Dimension containerDimension){
        System.out.println(imgDimension);
        Dimension dimension = new Dimension();
        if(DisplayInfo.isVertical(imgDimension)){
            final double newWidth = imgDimension.getWidth()*containerDimension.getHeight()/imgDimension.getHeight();
            dimension.setSize(containerDimension.getHeight(), newWidth);
        }else{
            final double newHeight = imgDimension.getHeight()*containerDimension.getWidth()/imgDimension.getWidth();
            dimension.setSize(newHeight, containerDimension.getWidth());
        }
        return dimension;
    }
    public static boolean isVertical(Dimension dimension){
        return dimension.getHeight() > dimension.getWidth();
    }
}
