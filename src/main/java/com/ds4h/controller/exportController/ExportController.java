package com.ds4h.controller.exportController;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.projectManager.exportProject.ExportProject;

import java.io.IOException;

public class ExportController {

    private ExportController(){

    }

    public static void exportProject(final CornerManager cornerManager, final String path) throws IOException {
        if(!cornerManager.getCornerImages().isEmpty()) {
            ExportProject.exportProject(cornerManager, path);
        }
    }
}
