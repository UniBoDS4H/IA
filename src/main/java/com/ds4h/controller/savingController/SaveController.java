package com.ds4h.controller.savingController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.saveProject.SaveImages;
import com.ds4h.model.util.saveProject.saveReferenceMatrix.SaveMatrix;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class SaveController {
    //TODO:ADD DOC

    /**
     *
     * @param images a
     * @param path b
     * @throws IOException c
     */
    public static void saveImages(final List<AlignedImage> images, final String path) throws IOException, IllegalArgumentException {
        if(!images.isEmpty()) {
            SaveImages.saveImages(images, path);
        }else {
            throw new IllegalArgumentException("You must choose at least one image.");
        }
    }
}
