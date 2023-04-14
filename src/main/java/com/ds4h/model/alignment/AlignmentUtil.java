package com.ds4h.model.alignment;

import com.ds4h.model.alignment.alignmentAlgorithm.*;

import java.util.Objects;

/**
 *
 */
public class AlignmentUtil {

    /**
     *
     */
    private AlignmentUtil(){}

    /**
     * Returns the algorithm that will be used for the alignment.
     * @param alg the selected algorithm.
     * @return the algorithm that will be used for the alignment.
     */
    public static AlignmentAlgorithmEnum getEnumFromAlgorithm(final AlignmentAlgorithm alg){
        if(Objects.nonNull(alg)) {
            if (alg instanceof TranslationalAlignment) {
                return AlignmentAlgorithmEnum.TRANSLATIONAL;
            } else if (alg instanceof ProjectiveAlignment) {
                return AlignmentAlgorithmEnum.PROJECTIVE;
            } else if (alg instanceof AffineAlignment) {
                return AlignmentAlgorithmEnum.AFFINE;
            }
            throw new IllegalArgumentException("Algorithm not present");
        }
        throw new NullPointerException("Algorithm is NULL");
    }
}
