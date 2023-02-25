package com.ds4h.model.alignment;

public enum AlignmentAlgorithmEnum {

    TRANSLATIVE("Translative", "Translative alignment is a geometric transformation that can be used to align two images by applying a translation in x and y directions. It is the simplest form of image alignment, and it assumes that the images are already approximately aligned and only need to be shifted by a certain amount in each direction."),
    PROJECTIVE("Projective", ""),
    AFFINE("Affine", "Affine alignment is a geometric transformation that can be used to align two images by applying a combination of translation, rotation, scaling, and shearing. Unlike homography alignment, affine alignment does not require that the scene in the images is planar, and it can handle a wider range of transformations between the two images");
    private String type;
    private String documentation;
    AlignmentAlgorithmEnum(final String type, final String documentation){

        this.type = type;
        this.documentation = documentation;
    }

    public String getType(){
        return this.type;
    }
    @Override
    public String toString(){
        return this.type;
    }
}
