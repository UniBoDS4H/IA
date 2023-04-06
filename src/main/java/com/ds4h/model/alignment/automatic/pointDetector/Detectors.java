package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.alignment.automatic.pointDetector.akazeDetector.AKAZEDetector;
import com.ds4h.model.alignment.automatic.pointDetector.briskDetector.BRISKDetector;
import com.ds4h.model.alignment.automatic.pointDetector.kazeDetector.KAZEDetector;
import com.ds4h.model.alignment.automatic.pointDetector.orbDetector.ORBDetector;
import com.ds4h.model.alignment.automatic.pointDetector.siftDetector.SIFTDetector;
import com.ds4h.model.alignment.automatic.pointDetector.surfDetector.SURFDetector;
import ij.IJ;

import java.awt.*;

public enum Detectors {

    AKAZE("AKAZE", new AKAZEDetector(), 0),
    BRISK("BRISK", new BRISKDetector(), 0),
    KAZE("KAZE", new KAZEDetector(), 0),
    ORB("ORB", new ORBDetector(), 0),
    SIFT("SIFT", new SIFTDetector(), 0),
    SURF("SURF", new SURFDetector(), 0);

    private final String name;
    private final PointDetector pointDetector;
    private double factor;

    private int scaling;

    public static final int LOWER_BOUND = PointDetector.LOWER_BOUND;
    public static final int UPPER_BOUND = PointDetector.UPPER_BOUND;

    Detectors(final String name, final PointDetector pointDetector, final double factor){
        this.name = name;
        this.pointDetector = pointDetector;
        this.factor = factor;
        this.scaling = PointDetector.DEFAULT;
    }


    public void setFactor(final double factor){
        if(factor >= 0){
            this.factor = (factor/10);
        }
    }

    public void setScaling(final int scaling){
        if(scaling >= Detectors.LOWER_BOUND && scaling <= Detectors.UPPER_BOUND){
            this.scaling = scaling;
            IJ.log("[DETECTORS] Set scaling: " + scaling);
        }
    }
    public double getFactor(){
        return this.factor;
    }

    public int getScaling(){
        return this.scaling;
    }

    public PointDetector pointDetector(){
        return this.pointDetector;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
