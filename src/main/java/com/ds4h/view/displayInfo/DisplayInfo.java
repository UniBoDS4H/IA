package com.ds4h.view.displayInfo;

import java.awt.*;

public final class DisplayInfo {
    private DisplayInfo(){}

    public static int getDisplayHeight(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.height;
    }
    public static int getDisplayWidth(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.width;
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
