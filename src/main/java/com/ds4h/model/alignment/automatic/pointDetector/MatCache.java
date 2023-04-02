package com.ds4h.model.alignment.automatic.pointDetector;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Objects;

public class MatCache {
    private Mat targetMatrix;

    public MatCache(){
        this.targetMatrix = null;
    }

    public void setTargetMatrix(final Mat matrix){
        this.targetMatrix = Objects.requireNonNull(matrix);
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
