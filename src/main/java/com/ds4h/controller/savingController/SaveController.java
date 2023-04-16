package com.ds4h.controller.savingController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.saveProject.SaveImages;
import com.ds4h.model.util.saveProject.saveReferenceMatrix.SaveMatrix;

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
     * @throws IllegalArgumentException if the list of images is empty.
     */
    public static void saveImages(final List<AlignedImage> images, final String path) throws IllegalArgumentException {
        if(!images.isEmpty()) {
            SaveImages.saveImages(images, path);
        }else {
            throw new IllegalArgumentException("You must choose at least one image.");
        }
    }
}