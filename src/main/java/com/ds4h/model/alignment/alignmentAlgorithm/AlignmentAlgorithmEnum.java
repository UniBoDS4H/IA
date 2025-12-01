package com.ds4h.model.alignment.alignmentAlgorithm;

import com.drew.lang.annotations.NotNull;

public enum AlignmentAlgorithmEnum {

    AFFINE("Affine", "At least " + AffineAlignment.LOWER_BOUND + " corresponding point(s) for each image is required."),

    PROJECTIVE("Projective", "At least " + ProjectiveAlignment.LOWER_BOUND + " corresponding point(s) for each image is required."),
    TRANSLATIONAL("Translational", "At least " + TranslationalAlignment.LOWER_BOUND +" corresponding point(s) for each image is required.");


    private final String type;
    private final String documentation;
    AlignmentAlgorithmEnum(@NotNull final String type,
                           @NotNull final String documentation){

        this.type = type;
        this.documentation = documentation;
    }

    @NotNull
    public String getDocumentation(){
        return this.documentation;
    }

    @NotNull
    public String getType(){
        return this.type;
    }
    @Override
    @NotNull
    public String toString(){
        return this.type;
    }
}
