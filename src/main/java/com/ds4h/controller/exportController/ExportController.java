package com.ds4h.controller.exportController;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.export.ZipExporter;

import java.io.IOException;
import java.util.List;

import com.ds4h.model.util.export.ZipExporter.*;

public class ExportController {

    private ExportController(){

    }

    public static void exportAsZip(final List<AlignedImage> images, final String path) throws IOException {
        ZipExporter.exportToZip(images, path);
    }
}
