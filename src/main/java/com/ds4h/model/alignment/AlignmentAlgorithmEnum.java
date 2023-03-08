package com.ds4h.model.alignment;

import com.ds4h.model.alignment.alignmentAlgorithm.TranslationalAlignment;

public enum AlignmentAlgorithmEnum {
    TRANSLATIONAL("Translational", TranslationalAlignment.LOWER_BOUND +" Translation alignment is a geometric transformation that can\n" +
            " be used to align two images by applying a translation in x and y directions.\n" +
            " It is the simplest form of image alignment, and it assumes that the images are already approximately\n" +
            " aligned and only need to be shifted by a certain amount in each direction.");


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
