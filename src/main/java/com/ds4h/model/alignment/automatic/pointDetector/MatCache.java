package com.ds4h.model.alignment.automatic.pointDetector;

import ij.IJ;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Objects;

public class MatCache {
    private Mat targetMatrix;

    public MatCache(){
        this.targetMatrix = null;
    }

    public Mat setTargetMatrix(final Mat matrix){
        this.targetMatrix = Objects.requireNonNull(matrix);
        IJ.log("[MAT CACHE] Cached: " + targetMatrix);
        return this.targetMatrix;
    }

    public Mat getTargetMatrix(){
        IJ.log("[MAT CACHE] Get Cached: " + this.targetMatrix);
        return this.targetMatrix;
    }

    public boolean isSet(){
        return Objects.nonNull(this.targetMatrix);
    }

    public void releaseMatrix(){
        if(Objects.nonNull(this.targetMatrix)){
            this.targetMatrix.release();
        }
    }
}
