package com.ds4h.model.alignment;

import com.ds4h.model.alignment.manual.AffineAlignment;
import com.ds4h.model.alignment.manual.PerspectiveAlignment;
import com.ds4h.model.alignment.manual.RansacAlignment;
import com.ds4h.model.alignment.manual.TranslationAlignment;

public enum AlignmentAlgorithmEnum {

    TRANSLATION("Translation", TranslationAlignment.LOWER_BOUND +" Translation alignment is a geometric transformation that can\n" +
            " be used to align two images by applying a translation in x and y directions.\n" +
            " It is the simplest form of image alignment, and it assumes that the images are already approximately\n" +
            " aligned and only need to be shifted by a certain amount in each direction."),
    PERSPECTIVE("Perspective", PerspectiveAlignment.LOWER_BOUND +"Perspective alignment is a technique used to align two images\n" +
            " that have different perspectives or viewpoints. The algorithm works by identifying corresponding points\n" +
            " in the two images, which are usually features that can be detected automatically (such as corners, edges, or blobs).\n" +
            " The algorithm then uses these corresponding points to estimate a homography matrix, which describes the transformation\n" +
            " between the two images."),
    AFFINE("Affine", AffineAlignment.REQUIRED_POINTS+ "Affine alignment is a geometric transformation that can be used to align two images\n" +
            " by applying a combination of translation, rotation, scaling, and shearing. Unlike homography alignment,\n" +
            " affine alignment does not require that the scene in the images is planar,\n" +
            " and it can handle a wider range of transformations between the two images"),
    RANSAC("RANSAC", String.valueOf(RansacAlignment.LOWER_BOUND));
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
