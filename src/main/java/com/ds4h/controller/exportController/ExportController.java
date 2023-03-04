package com.ds4h.controller.exportController;

import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.util.projectManager.exportProject.ExportProject;

import java.io.IOException;

public class ExportController {

    private ExportController(){

    }

    public static void exportProject(final PointManager pointManager, final String path) throws IOException {
        if(!pointManager.getCornerImages().isEmpty()) {
            ExportProject.exportProject(pointManager, path);
        }
    }
}
