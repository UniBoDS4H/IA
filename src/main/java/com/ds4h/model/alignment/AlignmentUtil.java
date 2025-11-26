package com.ds4h.model.alignment;

import com.drew.lang.annotations.NotNull;
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
    @NotNull
    public static AlignmentAlgorithmEnum getEnumFromAlgorithm(@NotNull final AlignmentAlgorithm alg){
        if (alg instanceof TranslationalAlignment) {
            return AlignmentAlgorithmEnum.TRANSLATIONAL;
        } else if (alg instanceof ProjectiveAlignment) {
            return AlignmentAlgorithmEnum.PROJECTIVE;
        } else if (alg instanceof AffineAlignment) {
            return AlignmentAlgorithmEnum.AFFINE;
        }
    }
}
