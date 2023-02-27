package com.ds4h.view.cornerSelectorGUI;

import com.ds4h.model.imageCorners.ImageCorners;

public class MenuItem {
    private final int index;
    private final ImageCorners image;
    public MenuItem(int index, ImageCorners image){
        this.index = index;
        this.image = image;
    }
    public ImageCorners getImage(){
        return this.image;
    }

    @Override
    public String toString() {
        return this.index + " - " + this.image;
    }
}
