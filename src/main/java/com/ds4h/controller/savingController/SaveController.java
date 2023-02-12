package com.ds4h.controller.savingController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.save.SaveImages;

import java.io.IOException;
import java.util.List;

public class SaveController {

    public static void saveImages(final List<AlignedImage> images, final String path) throws IOException {
        SaveImages.saveImages(images, path);
    }
}
