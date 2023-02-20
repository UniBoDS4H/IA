package com.ds4h.model.deformation;

import bunwarpj.bUnwarpJ_;
import ij.ImagePlus;
import bunwarpj.Transformation;

/**
 * This class is used in order to apply an elastic transformation using the BUnwarpJ Library.
 */
public class BunwarpjDeformation {
    private BunwarpjDeformation(){

    }
    /**
     * Use BunwarpJ in order to apply the elastic transformation.
     * @param mode : The registration mode can be “Accurate”, “Fast” and “Mono”. The registration mode “Mono”. By using "Mono" you have unidirectional registration
     * @param img_subsamp_fact : The image dimensions can be reduced by a factor of 2^0 = 1 to 2^7 = 128.
     * @param min_scale_deformation : Min scale deformation
     * @param max_scale_deformation : Max scale deformation
     * @param divWeight : Divergence Weight
     * @param curlWeight : Curl Weight
     * @param landmarkWeight : Landmark Weight. It forces the deformations to fit the landmark points on both images. Set it to 1.0 unless you’re not using landmarks.
     * @param imageWeight : Image Weight. This is the weight to control the pixel values differences. Leave it to 1.0 unless you want to do for instance landmark-only registration.
     * @param consistencyWeight : Consistency Weight. The divergence and curl weights regularize the deformation by penalizing the Divergence and Curl (mathematics) of the deformation vector field. In other words, we penalize vector fields with many points like this:
     * @param threshold : Stop Threshold
     * @param target : Target Image
     * @param source : Source Image
     * @return :
     */
    public static ImagePlus deform(final int mode, final int img_subsamp_fact, final int min_scale_deformation,
            final int max_scale_deformation, final int divWeight, final int curlWeight, final int landmarkWeight,
            final int imageWeight, final int consistencyWeight, final int threshold, final ImagePlus target, final ImagePlus source){
        //Compute the tranformation
        final Transformation transformation = bUnwarpJ_.computeTransformationBatch(target,
                source,
                target.getProcessor(),
                source.getProcessor(),
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
        return transformation.getDirectResults();
    }
}
