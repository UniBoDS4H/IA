package com.ds4h.view.cornerSelectorGUI;

import ij.gui.OvalRoi;
import ij.gui.PointRoi;
import ij.process.ImageProcessor;

import java.awt.*;

public class IApointROI extends PointRoi {
    public IApointROI(int x, int y){
        super(x,y);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        int x = (int)this.getXBase();
        int y = (int)this.getYBase();
        g.setColor(this.getStrokeColor());
        g.drawOval(x-20,y-20, 40,40);
    }
}
