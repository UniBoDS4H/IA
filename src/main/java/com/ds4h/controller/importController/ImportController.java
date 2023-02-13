package com.ds4h.controller.importController;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.importProject.ImportProject;

import java.io.File;
import java.io.FileNotFoundException;

public class ImportController {
    private ImportController(){

    }

    public static void importProject(final File directory, final CornerManager cornerManager) throws FileNotFoundException {
        //TODO: CHECK IF IT IS A DIRECTORY
        cornerManager.addImages(ImportProject.importProject(directory));
    }
}