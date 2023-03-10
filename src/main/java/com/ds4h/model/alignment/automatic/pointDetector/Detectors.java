package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.alignment.automatic.pointDetector.briskDetector.BRISKDetector;
import com.ds4h.model.alignment.automatic.pointDetector.kazeDetector.KAZEDetector;
import com.ds4h.model.alignment.automatic.pointDetector.surfDetector.SURFDetector;

import java.awt.*;

public enum Detectors {
    BRISK("BRISK", new BRISKDetector()),
    KAZE("KAZE", new KAZEDetector()),
    SURF("SURF", new SURFDetector());

    private final String name;
    private final PointDetector pointDetector;

    Detectors(final String name, final PointDetector pointDetector){
        this.name = name;
        this.pointDetector = pointDetector;
    }

    public PointDetector pointDetector(){
        return this.pointDetector;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
