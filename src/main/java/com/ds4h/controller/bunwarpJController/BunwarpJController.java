package com.ds4h.controller.bunwarpJController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.deformation.BunwarpjDeformation;
import ij.ImagePlus;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller for the BunwarpJ elastic deformation
 */
public class BunwarpJController {

    public BunwarpJController(){

    }

    /**
     * Use BunwarpJ in order to apply the elastic transformation using the Class Model.
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
     * @param cornerManager : Target Image
     * @return : the transformed image
     */
    public ImagePlus transformation(final int mode, final int img_subsamp_fact, final int min_scale_deformation,
                                    final int max_scale_deformation, final int divWeight, final int curlWeight, final int landmarkWeight,
                                    final int imageWeight, final int consistencyWeight, final int threshold
            , final CornerManager cornerManager){
        return BunwarpjDeformation.deform(mode, img_subsamp_fact, min_scale_deformation, max_scale_deformation, divWeight,
                curlWeight, landmarkWeight, imageWeight, consistencyWeight, threshold, cornerManager.getImagesToAlign().get(0).getImage(), cornerManager.getSourceImage().get().getImage());
    }

    /**
     * Use BunwarpJ in order to apply the elastic transformation using the Class Model.
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
     * @param images : the list of images to deform
     * @return : the transformed image
     */
    public List<ImagePlus> transformation(final int mode, final int img_subsamp_fact, final int min_scale_deformation,
                                          final int max_scale_deformation, final int divWeight, final int curlWeight, final int landmarkWeight,
                                          final int imageWeight, final int consistencyWeight, final int threshold
            , final List<AlignedImage> images){
        final AlignedImage source = images.stream().filter(alignedImage -> !alignedImage.getRegistrationMatrix().isPresent()).findFirst().get();
        final List<ImagePlus> imagePlusList = new LinkedList<>();
        images.stream().map(AlignedImage::getAlignedImage).forEach(imagePlus -> {
            final ImagePlus deformedImage = BunwarpjDeformation.deform(mode, img_subsamp_fact, min_scale_deformation, max_scale_deformation, divWeight,
                    curlWeight, landmarkWeight, imageWeight, consistencyWeight, threshold, imagePlus, source.getAlignedImage());
            imagePlusList.add(deformedImage);
        });
        return imagePlusList;
    }
}
