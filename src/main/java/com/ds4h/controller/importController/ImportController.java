package com.ds4h.controller.importController;

import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.util.MemoryController;
import com.ds4h.model.util.projectManager.importProject.ImportProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class ImportController {
    private ImportController(){

    }

    public static void importProject(final File directory, final PointManager pointManager) throws FileNotFoundException {
        if(directory.isDirectory() && Objects.nonNull(pointManager)) {
            final List<ImagePoints> images = (ImportProject.importProject(directory));
            pointManager.addImages(images);

            if(ImportProject.getTargetImage().isPresent()){
                pointManager.setAsSource(ImportProject.getTargetImage().get());
            }
            MemoryController.controllMemory(pointManager.getCornerImages());
            if(images.size() == 0){
                throw new FileNotFoundException("The directory chosen does not contain any type of images.\n" +
                        "Please select a directory with images.");
            }
            images.clear();
        }
    }
}
