package com.ds4h.model.alignment.automatic.pointDetector;

import ij.IJ;
import org.opencv.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @see com.ds4h.model.alignment.Alignment
 * @see com.ds4h.model.alignment.automatic.pointDetector.PointDetector
 * This class is used inside the Automatic Alignment, more specifically inside the Point Detector in order to detect and compute
 * only one time the Mat Descriptor and the MatOfKeyPoints for the selected Target.
 */
public class MatCache {
    private Mat descriptor;
    private final List<KeyPoint> keyPointList;

    /**
     * Constructor for the MatCache.
     */
    public MatCache(){
        this.descriptor = null;
        this.keyPointList = new ArrayList<>(100);
    }

    /**
     * With this method we save the Mat Descriptor and the MatOfKeyPoint, in order to be used for all the images in the alignment.
     * @see com.ds4h.model.alignment.automatic.pointDetector.PointDetector
     * @param descriptor Mat descriptor detected with the selected Point Detector.
     * @param keyPoints All the KeyPoints detected with the selected Point Detector.
     */
    public void setDetection(final Mat descriptor, final MatOfKeyPoint keyPoints){
        this.descriptor = Objects.requireNonNull(descriptor);
        this.keyPointList.addAll(Objects.requireNonNull(keyPoints).toList());
    }

    /**
     *
     * @return The Mat Descriptor
     */
    public Mat getDescriptor(){
        return this.descriptor;
    }

    /**
     *
     * @return The List of all KeyPoints.
     */
    public List<KeyPoint> getKeyPoints(){
        return this.keyPointList;
    }

    /**
     *
     * @return True if the Descriptor and the KeyPoints are already set.
     */
    public boolean isAlreadyDetected(){
        return Objects.nonNull(this.descriptor) && !this.keyPointList.isEmpty();
    }

    /**
     * With the using of this method we release the Mat Descriptor, and we clear the List of all KeyPoints.
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
