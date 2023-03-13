package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.alignment.automatic.pointDetector.akazeDetector.AKAZEDetector;
import com.ds4h.model.alignment.automatic.pointDetector.briskDetector.BRISKDetector;
import com.ds4h.model.alignment.automatic.pointDetector.kazeDetector.KAZEDetector;
import com.ds4h.model.alignment.automatic.pointDetector.orbDetector.ORBDetector;
import com.ds4h.model.alignment.automatic.pointDetector.siftDetector.SIFTDetector;
import com.ds4h.model.alignment.automatic.pointDetector.surfDetector.SURFDetector;

import java.awt.*;

public enum Detectors {
    BRISK("BRISK", new BRISKDetector(), 0),
    KAZE("KAZE", new KAZEDetector(), 0),
    ORB("ORB", new ORBDetector(), 0),
    SIFT("SIFT", new SIFTDetector(), 0),
    SURF("SURF", new SURFDetector(), 0);

    private final String name;
    private final PointDetector pointDetector;
    private double factor;

    Detectors(final String name, final PointDetector pointDetector, final double factor){
        this.name = name;
        this.pointDetector = pointDetector;
        this.factor = factor;
    }

    public void setFactor(final int factor){
        if(factor >= 0){
            this.factor = (double)(factor/10.0);
        }
    }
    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = (factor/10);
        }
    }
    public double getFactor(){
        return this.factor;
    }

    public PointDetector pointDetector(){
        return this.pointDetector;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
