package com.ds4h.model.alignment.automatic.pointDetector;

import ij.IJ;
import org.opencv.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class MatCache {
    private Mat descriptor;
    private final List<KeyPoint> keyPointList;

    /**
     *
     */
    public MatCache(){
        this.descriptor = null;
        this.keyPointList = new ArrayList<>(100);
    }

    /**
     *
     * @param descriptor a
     * @param keyPoints b
     */
    public void setDetection(final Mat descriptor, final MatOfKeyPoint keyPoints){
        this.descriptor = Objects.requireNonNull(descriptor);
        this.keyPointList.addAll(Objects.requireNonNull(keyPoints).toList());
    }

    /**
     *
     * @return a
     */
    public Mat getDescriptor(){
        return this.descriptor;
    }

    /**
     *
     * @return b
     */
    public List<KeyPoint> getKeyPoints(){
        return this.keyPointList;
    }

    /**
     *
     * @return a
     */
    public boolean isAlreadyDetected(){
        return Objects.nonNull(this.descriptor) && !this.keyPointList.isEmpty();
    }

    /**
     *
     */
    public void releaseMatrix(){
        if(this.isAlreadyDetected()){
            IJ.log("[MAT CACHE] Release Points");
            this.descriptor.release();
            this.descriptor = null;
            this.keyPointList.clear();
            System.gc();
        }
    }
}
