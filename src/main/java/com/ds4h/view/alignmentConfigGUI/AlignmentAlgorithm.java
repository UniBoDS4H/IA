package com.ds4h.view.alignmentConfigGUI;

public enum AlignmentAlgorithm {

    TRANSLATIVE("Translative"),
    PROJECTIVE("Projective"),
    AFFINE("Affine");
    private String type;
    AlignmentAlgorithm(final String type){
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
