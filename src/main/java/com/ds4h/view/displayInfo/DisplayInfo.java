package com.ds4h.view.displayInfo;


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
        Dimension dimension = new Dimension();
        if(DisplayInfo.isVertical(imgDimension)){
            double newWidth = imgDimension.getWidth()*containerDimension.getHeight()/imgDimension.getHeight();
            double newHeight = containerDimension.getHeight();
            if(newWidth > containerDimension.getWidth()){
                final double temp = newWidth;
                newWidth = containerDimension.getWidth();
                newHeight = newWidth*newHeight/temp;
            }
            dimension.setSize(newWidth,newHeight);
        }else{
            double newHeight = imgDimension.getHeight()*containerDimension.getWidth()/imgDimension.getWidth();
            double newWidth = containerDimension.getWidth();
            if(newHeight > containerDimension.getHeight()){
                final double temp = newHeight;
                newHeight = containerDimension.getHeight();
                newWidth = newHeight*newWidth/temp;
            }
            dimension.setSize(newWidth,newHeight);
        }
        return dimension;
    }
    public static boolean isVertical(Dimension dimension){
        return dimension.getHeight() > dimension.getWidth();
    }

    public static int getTextSize(int size){
        int s = (int)(size*DisplayInfo.getDisplaySize().getWidth()/400);
        System.out.println(s);
        return s;
    }

}
