package com.ds4h.model.deformation;

import bunwarpj.BSplineModel;
import bunwarpj.bUnwarpJ_;
import ij.ImagePlus;
import bunwarpj.Transformation;

public class BunwarpjDeformation {


    public static ImagePlus deform(final ImagePlus img1,final ImagePlus img2){
        int mode = 0;
        int subsmap_fact = 0;
        int min_scale_deformation = 0;
        int max_scale_deformation = 0;
        int div = 0, curl = 0,  landmark = 0, image = 0,cons = 0, thr = 0;

        final Transformation transformation = bUnwarpJ_.computeTransformationBatch(img1,
                img2,
                img1.getProcessor(),
                img2.getProcessor(),
                mode,
                    subsmap_fact,
                min_scale_deformation,
                max_scale_deformation,
                div,
                curl,
                landmark,
                image,
                cons,
                thr);
        ImagePlus output = transformation.getDirectResults();
        return output;
    }
}
