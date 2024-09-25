package com.ds4h.controller.savingController;

import com.ds4h.controller.pointController.PointController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.saveProject.SaveImages;
import com.ds4h.model.util.saveProject.saveReferenceMatrix.SaveMatrix;
import com.ds4h.view.util.SaveAsEnum;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Save Controller in order to call the Save method from the Model.
 */
public class SaveController {

    /**
     * Save all the "aligned images" in the selected "path".
     * @param images the aligned images to be stored.
     * @param path the path where the images are stored.
     * @param saveAsType the type of saving (project, TIFF file, Mosaic...).
     * @throws IllegalArgumentException if the list of images is empty.
     */
    public static void saveImages(final List<AlignedImage> images, final String path, final SaveAsEnum saveAsType,
                                  final boolean isOrderAscending, final boolean isTargetImageForeground) throws IllegalArgumentException {
        if(!images.isEmpty()) {
            switch (saveAsType) {
                case SAVE_AS_PROJECT:
                    SaveImages.saveImages(images, path, com.ds4h.controller.savingController.SaveAsEnum.SAVE_AS_PROJECT,
                            isOrderAscending, isTargetImageForeground);
                    break;
                case SAVE_AS_TIFF:
                    SaveImages.saveImages(images, path, com.ds4h.controller.savingController.SaveAsEnum.SAVE_AS_TIFF,
                            isOrderAscending, isTargetImageForeground);
                    break;
                case SAVE_AS_MOSAIC:
                    SaveImages.saveImages(images, path, com.ds4h.controller.savingController.SaveAsEnum.SAVE_AS_MOSAIC,
                            isOrderAscending, isTargetImageForeground);
                    break;
                case SAVE_AS_COMPOSITE:
                    SaveImages.saveImages(images, path, com.ds4h.controller.savingController.SaveAsEnum.SAVE_AS_COMPOSITE,
                            isOrderAscending, isTargetImageForeground);
                    break;
            }
        }else {
            throw new IllegalArgumentException("You must choose at least one image.");
        }
    }
}