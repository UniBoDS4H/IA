package com.ds4h.controller.exportController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.exportProject.ExportProject;

import java.io.IOException;
import java.util.List;

public class ExportController {

    private ExportController(){

    }

    public static void exportProject(final List<ImageCorners> images, final String path) throws IOException {
        ExportProject.exportProject(images, path);
    }
}
