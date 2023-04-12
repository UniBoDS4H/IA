package com.ds4h.model.alignment.automatic.pointDetector;

import com.ds4h.model.alignment.automatic.pointDetector.orbDetector.ORBDetector;
import com.ds4h.model.alignment.automatic.pointDetector.siftDetector.SIFTDetector;
import com.ds4h.model.alignment.automatic.pointDetector.surfDetector.SURFDetector;
import ij.IJ;

/**
 * Inside this Enum, we have all the Point Detectors implemented. With the using of this Enum we can adjust the
 * scaling factor and the threshold factor for the detection of points.
 */
public enum Detectors {

    //AKAZE("AKAZE", new AKAZEDetector(), 0),
    //BRISK("BRISK", new BRISKDetector(), 0),
    //KAZE("KAZE", new KAZEDetector(), 0),
    ORB("ORB", new ORBDetector(), 0),
    SIFT("SIFT", new SIFTDetector(), 0),
    SURF("SURF", new SURFDetector(), 0);

    private final String name;
    private final PointDetector pointDetector;
    private double thresholdFactor;
    private int scaling;
    public static final int LOWER_BOUND = PointDetector.LOWER_BOUND;
    public static final int UPPER_BOUND = PointDetector.UPPER_BOUND;

    /**
     * @see com.ds4h.model.alignment.automatic.pointDetector.PointDetector
     * @param name Name of the detector.
     * @param pointDetector The class detector.
     * @param factor The threshold factor, it is always initialized with 0.
     */
    Detectors(final String name, final PointDetector pointDetector, final double factor){
        this.name = name;
        this.pointDetector = pointDetector;
        this.thresholdFactor = factor;
        this.scaling = PointDetector.DEFAULT;
    }

    /**
     * Set the threshold factor for the point detector.
     * @param thresholdFactor how many "dirty" points the detector should take.
     */
    public void setThresholdFactor(final double thresholdFactor){
        if(thresholdFactor >= 0){
            this.thresholdFactor = (thresholdFactor /10);
        }
    }

    /**
     * Set the scaling factor for the point detector. The scaling is exponential so if in the input we choose 4 the images
     * will be scaled 16 times.
     * @param scaling how many times the images should be scaled.
     */
    public void setScaling(final int scaling){
        if(scaling >= Detectors.LOWER_BOUND && scaling <= Detectors.UPPER_BOUND){
            this.scaling = scaling;
            IJ.log("[DETECTORS] Set scaling: " + scaling);
        }
    }

    /**
     *
     * @return The Threshold Factor
     */
    public double getThresholdFactor(){
        return this.thresholdFactor;
    }

    /**
     *
     * @return The Scaling Factor
     */
    public int getScaling(){
        return this.scaling;
    }

    /**
     *
     * @return The Point Detector
     */
    public PointDetector pointDetector(){
        return this.pointDetector;
    }

    /**
     *
     * @return The name of the point detector.
     */
    @Override
    public String toString(){
        return this.name;
    }

}