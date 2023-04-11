package com.ds4h.model.util.projectManager.exportProject;

import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.imagePoints.ImagePoints;
import com.ds4h.model.util.directoryManager.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.json.jsonSerializer.JSONSerializer;
import com.ds4h.model.util.saveProject.SaveImages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExportProject {

    private final static String PROJECT_FOLDER = "DS4H_Project";
    private ExportProject(){

    }

    //TODO: add better doc
    /**
     * Export the entire project
     * @param pointManager the corner manager of all the images.
     * @param path the path where store the project
     * @throws IOException error in the saving s
     */
    public static void exportProject(final PointManager pointManager, final String path) throws IOException {

        final String directory = DirectoryCreator.createDirectory(path, PROJECT_FOLDER);
        if(!directory.isEmpty()){
            SaveImages.save(new ArrayList<>(pointManager.getCornerImages()).stream()
                    .peek(ImagePoints::clearPoints)
                    .map(ImagePoints::getImagePlus)
                    .collect(Collectors.toList()), path+"/"+directory);
            JSONSerializer.createJSON(pointManager, path+"/"+directory);
        }else{
            //Something happen, the creation failed I save the image inside the path.
            SaveImages.save(pointManager.getCornerImages().stream().map(ImagePoints::getImagePlus).collect(Collectors.toList()), path);
            JSONSerializer.createJSON(pointManager, path);
        }
    }


}
