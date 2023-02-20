package com.ds4h.model.util.exportProject;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.directoryCreator.DirectoryCreator;
import com.ds4h.model.util.json.jsonSerializer.JSONSerializer;
import com.ds4h.model.util.saveProject.SaveImages;
import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

//TODO : ADD COLORS INFORMATIONS FOR POINTS

public class ExportProject {

    private final static String PROJECT_FOLDER = "DS4H_Project";
    private ExportProject(){

    }

    //TODO: add better doc
    /**
     * Export the entire project
     * @param cornerManager the corner manager of all the images.
     * @param path the path where store the project
     * @throws IOException error in the saving s
     */
    public static void exportProject(final CornerManager cornerManager, final String path) throws IOException {

        final String directory = DirectoryCreator.createDirectory(path, PROJECT_FOLDER);
        if(!directory.isEmpty()){
            SaveImages.save(cornerManager.getCornerImages().stream().map(ImageCorners::getImage).collect(Collectors.toList()), path+"/"+directory);
            JSONSerializer.createJSON(cornerManager, path+"/"+directory);
        }else{
            //Something happen, the creation failed I save the image inside the path.
            SaveImages.save(cornerManager.getCornerImages().stream().map(ImageCorners::getImage).collect(Collectors.toList()), path);
            JSONSerializer.createJSON(cornerManager, path);
        }
    }


}
