package com.ds4h.view.pointSelectorGUI;

import com.ds4h.model.imagePoints.ImagePoints;

import java.util.HashMap;
import java.util.Map;

public class MenuItem {
    private final int index;
    private final ImagePoints image;
    public MenuItem(int index, ImagePoints image){
        this.index = index;
        this.image = image;
    }
    public ImagePoints getImage(){
        return this.image;
    }

    @Override
    public String toString() {
        return this.index + " - " + this.image;
    }
}
