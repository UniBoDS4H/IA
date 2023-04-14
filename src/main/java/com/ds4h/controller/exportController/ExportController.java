package com.ds4h.controller.exportController;

import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.util.projectManager.exportProject.ExportProject;

import java.io.IOException;

public class ExportController {

    private ExportController(){

    }

    /**
     * Export the project in the selected path.
     * @param pointManager where all the images are stored.
     * @param path where the project should be exported.
     * @throws IOException if the saving fails.
     */
    public static void exportProject(final PointManager pointManager, final String path) throws IOException {
        if(!pointManager.getPointImages().isEmpty()) {
            ExportProject.exportProject(pointManager, path);
        }
    }
}
