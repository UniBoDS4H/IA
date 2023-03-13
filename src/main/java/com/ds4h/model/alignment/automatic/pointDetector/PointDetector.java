package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.imagePoints.ImagePoints;

public abstract class PointDetector {

    private double factor = 0;

    public PointDetector(){

    }
    public abstract void detectPoint(final ImagePoints targetImage, final ImagePoints imagePoint);

    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = factor;
        }
    }

    public double getFactor(){
        return this.factor;
    }

}
