package com.ds4h.controller.savingController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.saveProject.SaveImages;
import com.ds4h.model.util.saveProject.saveReferenceMatrix.SaveMatrix;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SaveController {

    //TODO:ADD DOC
    public static void saveImages(final List<AlignedImage> images, final String path) throws IOException {
        SaveImages.saveImages(images, path);
    }
}
