package com.ds4h.view.standardGUI;

import ij.ImagePlus;
import ij.gui.ImageCanvas;

public class StandardCanvas extends ImageCanvas {
    private double firstMagnification;
    private boolean magnificated = false;

    public StandardCanvas(ImagePlus imp) {
        super(imp);
    }

    @Override
    public void zoomOut(int sx, int sy) {
        if(this.getMagnification() > this.firstMagnification){
            super.zoomOut(sx, sy);
        }
    }

    @Override
    public void setMagnification(double magnification) {
        if(!this.magnificated){
            this.firstMagnification = magnification;
            this.magnificated = true;
        }
        magnification = Math.max(magnification, this.firstMagnification);
        super.setMagnification(magnification);
    }
}
