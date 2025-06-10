package com.ds4h.model.util.projectManager.exportProject;

import com.ds4h.model.pointManager.ImageManager;
import com.ds4h.model.image.imagePoints.ImagePoints;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.json.jsonSerializer.JSONSerializer;
import com.ds4h.model.util.saveProject.SaveImages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExportProject {

    private final static String PROJECT_FOLDER = "DS4H_Project";
    private ExportProject(){

    }

    /**
     * Export the project with all the points in each image.
     * @param imageManager the corner manager of all the images.
     * @param path the path where store the project
     */
    public static void exportProject(final ImageManager imageManager, final String path) {

        final String directory = DirectoryCreator.createDirectory(path, PROJECT_FOLDER);
        final List<ImagePoints> copy = new ArrayList<>(imageManager.getPointImages().size());
        copy.addAll(imageManager.getPointImages());

        if(!directory.isEmpty()){
            SaveImages.save(copy.stream()
                    .map(ImagePoints::getImagePlus)
                    .collect(Collectors.toList()), path+"/"+directory);
            JSONSerializer.createJSON(imageManager, path+"/"+directory);
        }else{
            SaveImages.save(copy.stream()
                    .map(ImagePoints::getImagePlus)
                    .collect(Collectors.toList()), path);
            JSONSerializer.createJSON(imageManager, path);
        }
        copy.clear();
    }
}
