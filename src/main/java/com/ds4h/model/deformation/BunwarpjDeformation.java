package com.ds4h.model.deformation;

import bunwarpj.BSplineModel;
import bunwarpj.bUnwarpJ_;
import ij.ImagePlus;
import bunwarpj.Transformation;

public class BunwarpjDeformation {


    public static ImagePlus deform(final int mode, final int img_subsamp_fact, final int min_scale_deformation,
            final int max_scale_deformation, final int divWeight, final int curlWeight, final int landmarkWeight,
            final int imageWeight, final int consistencyWeight, final int threshold
            ,final ImagePlus img1,final ImagePlus img2){

        final Transformation transformation = bUnwarpJ_.computeTransformationBatch(img1,
                img2,
                img1.getProcessor(),
                img2.getProcessor(),
                mode,
                img_subsamp_fact,
                min_scale_deformation,
                max_scale_deformation,
                divWeight,
                curlWeight,
                landmarkWeight,
                imageWeight,
                consistencyWeight,
                threshold);
        final ImagePlus output = transformation.getDirectResults();
        return output;
    }
}
