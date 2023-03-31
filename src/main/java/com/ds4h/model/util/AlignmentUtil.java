package com.ds4h.model.util;

import com.ds4h.model.alignment.alignmentAlgorithm.*;

public class AlignmentUtil {
    private AlignmentUtil(){}

    public static AlignmentAlgorithmEnum getEnumFromAlgorithm(AlignmentAlgorithm alg){
        if(alg instanceof TranslationalAlignment){
            return AlignmentAlgorithmEnum.TRANSLATIONAL;
        }else if(alg instanceof ProjectiveAlignment){
            return AlignmentAlgorithmEnum.PROJECTIVE;
        }else if(alg instanceof AffineAlignment){
            return AlignmentAlgorithmEnum.AFFINE;
        }
        throw new IllegalArgumentException("Algorithm not present");
    }

}
