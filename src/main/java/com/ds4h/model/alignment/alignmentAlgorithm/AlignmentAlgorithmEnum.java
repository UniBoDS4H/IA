package com.ds4h.model.alignment.alignmentAlgorithm;

public enum AlignmentAlgorithmEnum {

    AFFINE("Affine", "At least " + AffineAlignment.LOWER_BOUND + " corresponding point(s) for each image is required."),

    PROJECTIVE("Projective", "At least " + ProjectiveAlignment.LOWER_BOUND + " corresponding point(s) for each image is required."),
    TRANSLATIONAL("Translational", "At least " + TranslationalAlignment.LOWER_BOUND +" corresponding point(s) for each image is required.");


    private String type;
    private String documentation;
    AlignmentAlgorithmEnum(final String type, final String documentation){

        this.type = type;
        this.documentation = documentation;
    }

    public String getDocumentation(){
        return this.documentation;
    }

    public String getType(){
        return this.type;
    }
    @Override
    public String toString(){
        return this.type;
    }
}
