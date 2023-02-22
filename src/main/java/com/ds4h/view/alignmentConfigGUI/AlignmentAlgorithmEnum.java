package com.ds4h.view.alignmentConfigGUI;

public enum AlignmentAlgorithmEnum {

    TRANSLATIVE("Translative"),
    PROJECTIVE("Projective"),
    AFFINE("Affine");
    private String type;
    AlignmentAlgorithmEnum(final String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
    @Override
    public String toString(){
        return this.type;
    }
}
