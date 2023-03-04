package com.ds4h.controller.importController;

import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.util.projectManager.importProject.ImportProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class ImportController {
    private ImportController(){

    }

    public static void importProject(final File directory, final PointManager pointManager) throws FileNotFoundException {
        if(directory.isDirectory() && Objects.nonNull(pointManager)) {
            pointManager.addImages(ImportProject.importProject(directory));
            if(ImportProject.getTargetImage().isPresent()){
                pointManager.setAsSource(ImportProject.getTargetImage().get());
            }
        }
    }
}
