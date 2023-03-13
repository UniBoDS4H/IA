package com.ds4h.model.alignment.alignmentAlgorithm;

public enum AlignmentAlgorithmEnum {

    AFFINE("Affine", "You must put: " + AffineAlignment.REQUIRED_POINTS + " points for each image."),

    PROJECTIVE("Projective", "You must pust at least: " + ProjectiveAlignment.LOWER_BOUND + " points for each image."),
    TRANSLATIONAL("Translational", "You must pust at least: " + TranslationalAlignment.LOWER_BOUND +" points for each image.");


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
