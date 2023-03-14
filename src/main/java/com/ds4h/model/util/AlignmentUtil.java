package com.ds4h.model.util;

import com.ds4h.model.alignment.alignmentAlgorithm.*;

public class AlignmentUtil {
    private static AlignmentAlgorithm translational = new TranslationalAlignment();
    private static AlignmentAlgorithm projective = new ProjectiveAlignment();
    private static AlignmentAlgorithm affine = new AffineAlignment();
    private AlignmentUtil(){}
    public static AlignmentAlgorithm getAlgorithmFromEnum(AlignmentAlgorithmEnum e){
        switch (e){
            case TRANSLATIONAL:
                return translational;
            case PROJECTIVE:
                return projective;
            case AFFINE:
                return affine;
        }
        throw new IllegalArgumentException("Algorithm not present");
    }

}
