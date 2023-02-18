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
//TODO : ADD INFORMATIONS ABOUT TARGET

public class ExportProject {

    private final static String PROJECT_NAME = "ds4h_project.json";
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

        final JSONArray imageList = JSONSerializer.createJSON(cornerManager);
        final String directory = DirectoryCreator.createDirectory(path, PROJECT_FOLDER);
        if(!directory.isEmpty()){
            SaveImages.save(cornerManager.getCornerImages().stream().map(ImageCorners::getImage).collect(Collectors.toList()), path+"/"+directory);
            ExportProject.exportJSON(imageList, path+"/"+directory);
        }else{
            //Something happen, the creation failed I save the image inside the path.
            SaveImages.save(cornerManager.getCornerImages().stream().map(ImageCorners::getImage).collect(Collectors.toList()), path);
            ExportProject.exportJSON(imageList, path);
        }
    }
    /*
            JSON FILE :
            {
                images = [
                            file_name : "image.tif",
                            points: [ XX:YY,
                                      XX,YY,
                                      .....
                                    ],
                              file_name : "image2.tif",
                              points: [ XX:YY,
                                        XX,YY,
                                        .....
                                       ],
                          ]
            }
         */

    /**
     * Write the JSONFile in the path
     * @param array all the project information
     * @param path where will be stored the inormation
     */
    private static void exportJSON(final JSONArray array, final String path){
        try (FileWriter file = new FileWriter(path+"/"+PROJECT_NAME)) {
            file.write(array.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
