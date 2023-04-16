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

    /**
     * Import a project from the input path.
     * @param directory directory where the project is imported.
     * @param pointManager where the images will be put.
     * @throws FileNotFoundException If the directory does not exist.
     * @throws OutOfMemoryError If the project size is bigger than the free memory available.
     */
    public static void importProject(final File directory, final PointManager pointManager) throws FileNotFoundException, OutOfMemoryError {
        if(directory.isDirectory() && Objects.nonNull(pointManager)) {
            final List<ImagePoints> images = (ImportProject.importProject(directory));
            pointManager.addImages(images);

            if(ImportProject.getTargetImage().isPresent()){
                pointManager.setAsTarget(ImportProject.getTargetImage().get());
            }
            if(images.size() == 0){
                throw new FileNotFoundException("The directory chosen does not contain any type of images.\n" +
                        "Please select a directory with images.");
            }
            MemoryController.controlMemory(pointManager.getPointImages());
            images.clear();
        }
    }
}
